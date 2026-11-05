package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.*;
import br.com.raizesnordeste.api.domain.enums.OrderStatus;
import br.com.raizesnordeste.api.domain.enums.ServiceType;
import br.com.raizesnordeste.api.domain.exception.BusinessRuleException;
import br.com.raizesnordeste.api.domain.repository.*;
import br.com.raizesnordeste.api.dto.request.OrderItemRequestDTO;
import br.com.raizesnordeste.api.dto.request.OrderRequestDTO;
import br.com.raizesnordeste.api.dto.request.PaymentMockRequestDTO;
import br.com.raizesnordeste.api.dto.response.OrderItemResponseDTO;
import br.com.raizesnordeste.api.dto.response.OrderResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final UnitProductRepository unitProductRepository;
    private final AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        UnitRepository unitRepository, ProductRepository productRepository,
                        UnitProductRepository unitProductRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.productRepository = productRepository;
        this.unitProductRepository = unitProductRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Criação de pedido:
     * - Congela o preço histórico do produto no ato da compra.
     * - Isenta a taxa de frete para compras KIOSK ou IN_PERSON.
     * - Gere o programa de fidelidade (1 ponto/compra e frete grátis a cada 10 pontos).
     * - Valida e deduz o estoque regional da filial.
     */
    @Transactional
    public OrderResponseDTO create(OrderRequestDTO request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado."));

        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new BusinessRuleException("Filial não encontrada."));

        Order order = new Order();
        order.setUser(user);
        order.setUnit(unit);
        order.setStatus(OrderStatus.AWAITING_PAYMENT); // Status padrão inicial do pedido "Aguardando pagamento".
        order.setPaymentMethod(request.paymentMethod());
        order.setServiceType(request.serviceType());

        // Validação de endereço: Dispensado para TOTEM (KIOSK) e Balcão (IN_PERSON)
        if (request.serviceType() != ServiceType.IN_PERSON && request.serviceType() != ServiceType.KIOSK) {
            if (request.deliveryAddressId() == null) {
                throw new BusinessRuleException("Para pedidos do tipo: " + request.serviceType() + ", o endereço de entrega é obrigatório.");
            }
            Address address = addressRepository.findById(request.deliveryAddressId())
                    .orElseThrow(() -> new BusinessRuleException("Endereço de entrega não encontrado."));

            if (!address.getUser().getId().equals(user.getId())) {
                throw new BusinessRuleException("O endereço de entrega informado não pertence a este usuário.");
            }
            order.setDeliveryAddress(address);
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemResponseDTO> responseItems = new ArrayList<>();

        for (OrderItemRequestDTO itemDTO : request.items()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new BusinessRuleException("Produto: " + itemDTO.productId() + " não encontrado."));

            // Validação de Catálogo e Estoque Regional
            UnitProduct unitProduct = unitProductRepository.findByUnitIdAndProductId(unit.getId(), product.getId())
                    .orElseThrow(() -> new BusinessRuleException("O produto '" + product.getName() + "' não está disponível nesta filial."));

            if (unitProduct.getQuantity() < itemDTO.quantity()) {
                throw new BusinessRuleException("Estoque insuficiente para o produto '" + product.getName() + "'. Disponível: " + unitProduct.getQuantity());
            }

            // Dedução imediata do estoque.
            unitProduct.setQuantity(unitProduct.getQuantity() - itemDTO.quantity());
            unitProductRepository.save(unitProduct);

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(itemDTO.quantity());
            orderProduct.setUnitPrice(product.getPrice()); // Garante o congelamento do preço no momento da compra.

            // Realiza o cálculo do subtotal e adiciona ao total.
            BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity()));
            totalAmount = totalAmount.add(subTotal);

            order.getItems().add(orderProduct);

            responseItems.add(new OrderItemResponseDTO(
                    product.getId(), product.getName(), itemDTO.quantity(), product.getPrice(), subTotal
            ));
        }

        order.setTotalAmount(totalAmount);

        // Sistema de Fidelidade e Frete
        if (order.getServiceType() == ServiceType.ONLINE || order.getServiceType() == ServiceType.PHONE) {

            if (user.getLoyaltyPoints() == null) user.setLoyaltyPoints(0);

            if (user.getLoyaltyPoints() >= 10) {
                user.setLoyaltyPoints(user.getLoyaltyPoints() - 10);
            } else {
                order.setTotalAmount(order.getTotalAmount().add(new BigDecimal("10.00")));
                user.setLoyaltyPoints(user.getLoyaltyPoints() + 1);
            }
            userRepository.save(user);
        }

        Order savedOrder = orderRepository.save(order);

        return new OrderResponseDTO(
                savedOrder.getId(), user.getId(), unit.getId(), savedOrder.getStatus(),
                savedOrder.getPaymentMethod(), savedOrder.getServiceType(), savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt(), responseItems
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll().stream().map(order -> {
            List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                    .map(item -> {
                        BigDecimal subTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        return new OrderItemResponseDTO(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                subTotal
                        );
                    }).toList();

            return new OrderResponseDTO(
                    order.getId(), order.getUser().getId(), order.getUnit().getId(),
                    order.getStatus(), order.getPaymentMethod(), order.getServiceType(),
                    order.getTotalAmount(), order.getCreatedAt(), itemDTOs
            );
        }).toList();
    }

    /**
     * Processa callback assíncrono de pagamento externo.
     */
    @Transactional
    public OrderResponseDTO processMockPayment(Long orderId, PaymentMockRequestDTO request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessRuleException("Pedido não encontrado."));

        if (order.getStatus() != OrderStatus.AWAITING_PAYMENT) {
            throw new BusinessRuleException("O pagamento não pode ser processado. Status atual: " + order.getStatus());
        }

        if (request.isApproved()) {
            order.setStatus(OrderStatus.PAID);
        }  else {
            order.setStatus(OrderStatus.CANCELED);
        }

        Order savedOrder = orderRepository.save(order);

        List<OrderItemResponseDTO> itemDTOs = savedOrder.getItems().stream()
                .map(item -> {
                    BigDecimal subTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    return new OrderItemResponseDTO(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            subTotal
                    );
                }).toList();

        return new OrderResponseDTO(
                savedOrder.getId(), savedOrder.getUser().getId(), savedOrder.getUnit().getId(),
                savedOrder.getStatus(), savedOrder.getPaymentMethod(), savedOrder.getServiceType(),
                savedOrder.getTotalAmount(), savedOrder.getCreatedAt(), itemDTOs
        );
    }
}
