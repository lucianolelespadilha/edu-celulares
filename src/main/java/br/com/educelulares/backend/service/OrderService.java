package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.OrderCreateDto;
import br.com.educelulares.backend.dto.OrderDto;
import br.com.educelulares.backend.dto.OrderItemCreateDto;
import br.com.educelulares.backend.dto.OrderItemDto;
import br.com.educelulares.backend.entity.Customer;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.OrderItem;
import br.com.educelulares.backend.entity.Product;
import br.com.educelulares.backend.exception.BadRequestException;
import br.com.educelulares.backend.exception.NotFoundException;
import br.com.educelulares.backend.repository.CustomerRepository;
import br.com.educelulares.backend.repository.OrderItemRepository;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/// -------------------------------------------------------------------------
// SERVIÇO RESPONSÁVEL PELO GERENCIAMENTO DE PEDIDOS (ORDERS)
// REALIZA AS OPERAÇÕES DE CRIAÇÃO, CONSULTA, LISTAGEM E EXCLUSÃO DE PEDIDOS
// -------------------------------------------------------------------------
@Service
@RequiredArgsConstructor
public class OrderService {

    // ---------------------------------------------------------------------
    // INJEÇÃO DOS REPOSITÓRIOS NECESSÁRIOS
    // ---------------------------------------------------------------------
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    // ---------------------------------------------------------------------
    // CRIA UM NOVO PEDIDO (ORDER)
    // ---------------------------------------------------------------------
    @Transactional
    public OrderDto createOrder(OrderCreateDto orderCreateDto) {

        // VERIFICA SE O ID DO CLIENTE FOI INFORMADO
        if (orderCreateDto.costumerId() == null) {
            throw new BadRequestException("CUSTOMER ID IS REQUIRED");
        }

        // BUSCA O CLIENTE NO BANCO DE DADOS OU LANÇA EXCEÇÃO CASO NÃO ENCONTRADO
        Customer customer = customerRepository.findById(orderCreateDto.costumerId())
                .orElseThrow(() -> new NotFoundException("CUSTOMER NOT FOUND"));

        // CRIA UM NOVO PEDIDO E DEFINE O CLIENTE
        Order order = new Order();
        order.setCustomer(customer);

        // GARANTE QUE A LISTA DE ITENS NÃO SEJA NULA
        var itemsDto = orderCreateDto.items() == null ? List.<OrderItemCreateDto>of() : orderCreateDto.items();

        // PERCORRE OS ITENS DO PEDIDO RECEBIDOS NO DTO
        for (OrderItemCreateDto itemDto : itemsDto) {

            // VALIDA SE O ITEM POSSUI PRODUTO E QUANTIDADE VÁLIDOS
            if (itemDto == null || itemDto.productId() == null || itemDto.quantity() == null) {
                throw new BadRequestException("PRODUCT ID OR QUANTITY IS REQUIRED");
            }

            // BUSCA O PRODUTO PELO ID INFORMADO OU LANÇA EXCEÇÃO CASO NÃO ENCONTRADO
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new NotFoundException("PRODUCT NOT FOUND: " + itemDto.productId()));

            // CRIA UM NOVO ITEM DE PEDIDO E CONFIGURA SUAS PROPRIEDADES
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);

            // ADICIONA O ITEM AO PEDIDO
            order.getItems().add(orderItem);
        }

        // SALVA O PEDIDO NO BANCO DE DADOS
        Order saved = orderRepository.save(order);

        // RETORNA O PEDIDO SALVO CONVERTIDO PARA DTO
        return toOrderDto(saved);
    }

    // ---------------------------------------------------------------------
    // RETORNA TODOS OS PEDIDOS CADASTRADOS
    // ---------------------------------------------------------------------
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // BUSCA UM PEDIDO PELO ID
    // ---------------------------------------------------------------------
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ORDER NOT FOUND"));
        return toOrderDto(order);
    }

    // ---------------------------------------------------------------------
    // EXCLUI UM PEDIDO PELO ID
    // ---------------------------------------------------------------------
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("ORDER NOT FOUND");
        }
        orderRepository.deleteById(id);
    }

    // ---------------------------------------------------------------------
    // CONVERTE A ENTIDADE ORDER EM DTO (OrderDto)
    // ---------------------------------------------------------------------
    private OrderDto toOrderDto(Order order) {

        // GARANTE QUE A LISTA DE ITENS NÃO SEJA NULA E CONVERTE CADA ITEM PARA DTO
        List<OrderItemDto> orderItems = Optional.ofNullable(order.getItems())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());

        // RETORNA O DTO COMPLETO DO PEDIDO
        return new OrderDto(
                order.getId(),
                order.getCustomer() == null ? null : order.getCustomer().getId(),
                orderItems,
                order.getCreatedAt()
        );
    }

    // ---------------------------------------------------------------------
    // CONVERTE A ENTIDADE ORDERITEM EM DTO (OrderItemDto)
    // ---------------------------------------------------------------------
    private OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getProduct() == null ? null : orderItem.getProduct().getId(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }
}
