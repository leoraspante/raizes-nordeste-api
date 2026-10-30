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

    @Transactional
    public OrderResponseDTO create(OrderRequestDTO request) {

        // Validação de Usuário e Filial.
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado."));

        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new BusinessRuleException("Filial não encontrada."));

        // Inicia-se o processo do pedido.
        Order order = new Order();
        order.setUser(user);
        order.setUnit(unit);
        order.setStatus(OrderStatus.AWAITING_PAYMENT); // Status padrão inicial do pedido "Aguardando pagamento".
        order.setPaymentMethod(request.paymentMethod());
        order.setServiceType(request.serviceType());

        // Validação do endereço de entrega.
        if (request.serviceType() != ServiceType.IN_PERSON) {

            if (request.deliveryAddressId() == null) {
                throw new BusinessRuleException("Para pedidos do tipo " + request.serviceType() + ", o endereço de entrega é obrigatório.");
            }

            Address address = addressRepository.findById(request.deliveryAddressId())
                    .orElseThrow(() -> new BusinessRuleException("Endereço de entrega não encontrado."));

            // Garante que o usuário não utilize o endereço de entrega pertencente a outro cliente.
            if (!address.getUser().getId().equals(user.getId())) {
                throw new BusinessRuleException("O endereço de entrega informado não pertence a este usuário.");
            }

            order.setDeliveryAddress(address);
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemResponseDTO> responseItems = new ArrayList<>();

        // Processa cada item do pedido.
        for (OrderItemRequestDTO itemDTO : request.items()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new BusinessRuleException("Produto: " + itemDTO.productId() + "não encontrado."));

            // Verifica se o produto pertence ao cardápio da filial.
            if (!unitProductRepository.existsByUnitIdAndProductId(unit.getId(), product.getId())) {
                throw new BusinessRuleException("O produto '" + product.getName() + "' não está disponível nesta filial.");
            }

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

        // Salva o pedido.
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

            // Mapeia os itens do pedido resgatando o preço histórico congelado
            List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                    .map(item -> {
                        BigDecimal subTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        return new OrderItemResponseDTO(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getUnitPrice(), // O preço que estava no dia da compra
                                subTotal
                        );
                    }).toList();

            return new OrderResponseDTO(
                    order.getId(),
                    order.getUser().getId(),
                    order.getUnit().getId(),
                    order.getStatus(),
                    order.getPaymentMethod(),
                    order.getServiceType(),
                    order.getTotalAmount(),
                    order.getCreatedAt(),
                    itemDTOs
            );
        }).toList();
    }

    @Transactional
    public OrderResponseDTO processMockPayment(Long orderId, PaymentMockRequestDTO request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessRuleException("Pedido não encontrado."));

        // Garante que somente sejam processados pagamentos de pedidos com status "AWAITING_PAYMENT".
        if (order.getStatus() != OrderStatus.AWAITING_PAYMENT) {
            throw new BusinessRuleException("O pagamento não pode ser processado. Status atual: " + order.getStatus());
        }

        // Simulação do Gateway de pagamento.
        if (request.isApproved()) {
            order.setStatus(OrderStatus.PAID);
        }  else {
            order.setStatus(OrderStatus.CANCELED);
        }

        Order savedOrder = orderRepository.save(order);

        // Padronização do JSON de retorno.
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
