# Raízes do Nordeste API

## Sobre o Projeto

API REST desenvolvida para a Atividade Eletiva IV - Projeto Multidisciplinar (Uninter).
O sistema gere as operações de uma rede de restaurantes, contemplando a gestão de filiais, catálogo de produtos, motor de vendas e regras de fidelidade. O projeto conta ainda com a simulação de integração de pagamentos (Webhook) e uma arquitetura segura protegida por JWT, respeitando princípios da LGPD.

## Arquitetura e Separação de Responsabilidades
O projeto adota uma arquitetura estruturada em camadas garantindo escalabilidade e manutenção facilitada:

* **Domain:** Representa o núcleo do sistema. Contém as entidades da base de dados, enums operacionais e os contratos de acesso a dados.
* **Application (Service):** Centraliza todas as regras de negócio, como o congelamento do histórico de preços, o motor de fidelidade (aplicação e isenção da taxa de entrega) e a máquina de estados para o fluxo de pagamentos simulados (Webhook).
* **Infrastructure:** Isola as complexidades técnicas do sistema, configurações de persistência em memória, blindagem da API (Stateless), filtros de interceptação e a gestão de tokens JWT.
* **API (Controllers & DTOs):** Camada de exposição dos endpoints RESTful auto-documentados via OpenAPI/Swagger. Utiliza DTOs imutáveis (Records) para o tráfego seguro de dados, validações rigorosas de entrada e um tratador global de exceções que agrupa e padroniza as respostas de erro para o cliente.

## Tecnologias Utilizadas

* **Java 17** + **Spring Boot 3**
* **Spring Security** + **Auth0 JWT** (Autenticação Stateless)
* **Base de Dados H2** (In-Memory para facilitar a avaliação)
* **Hibernate / Spring Data JPA**
* **Swagger / OpenAPI 3** (Documentação interativa)

## Como Executar Localmente

1. Certifique-se de ter o **Java 17** e o **Maven** instalados. Todas as dependências da aplicação estão declaradas no arquivo pom.xml.
2. Clone este repositório ou descompacte o projeto.
3. Abra o projeto na sua IDE de preferência e execute a classe principal do sistema.

