package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.OrderItemCreateDto;
import br.com.educelulares.backend.dto.OrderItemDto;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.OrderItem;
import br.com.educelulares.backend.entity.Product;
import br.com.educelulares.backend.repository.OrderItemRepository;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// -------------------------------------------------------------------------
// SERVIÇO RESPONSÁVEL PELO GERENCIAMENTO DOS ITENS DE PEDIDO (ORDER ITEMS)
// REALIZA OPERAÇÕES DE CRIAÇÃO, CONSULTA, ATUALIZAÇÃO E EXCLUSÃO
// -------------------------------------------------------------------------
@Service
@RequiredArgsConstructor
public class OrderItemService {

    // ---------------------------------------------------------------------
    // INJEÇÃO AUTOMÁTICA DOS REPOSITÓRIOS UTILIZADOS PELO SERVIÇO
    // ---------------------------------------------------------------------
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // ---------------------------------------------------------------------
    // CRIA UM NOVO ITEM DE PEDIDO
    // ---------------------------------------------------------------------
    @Transactional
    public OrderItemDto createOrderItem(OrderItemCreateDto orderItemCreateDto) {

        // -----------------------------------------------------------------
        // VALIDA SE O ID DO PEDIDO FOI INFORMADO
        // -----------------------------------------------------------------
        if (orderItemCreateDto.orderId() == null) {
            throw new IllegalArgumentException("ORDER ID IS REQUIRED");
        }

        // -----------------------------------------------------------------
        // VALIDA SE O ID DO PRODUTO FOI INFORMADO
        // -----------------------------------------------------------------
        if (orderItemCreateDto.productId() == null) {
            throw new IllegalArgumentException("PRODUCT ID IS REQUIRED");
        }

        // -----------------------------------------------------------------
        // BUSCA O PEDIDO PELO ID
        // -----------------------------------------------------------------
        Order order = orderRepository.findById(orderItemCreateDto.orderId())
                .orElseThrow(() -> new IllegalArgumentException("ORDER NOT FOUND"));

        // -----------------------------------------------------------------
        // BUSCA O PRODUTO PELO ID
        // -----------------------------------------------------------------
        Product product = productRepository.findById(orderItemCreateDto.productId())
                .orElseThrow(() -> new IllegalArgumentException("PRODUCT NOT FOUND"));

        // -----------------------------------------------------------------
        // CRIA E PREENCHE O ITEM DO PEDIDO
        // -----------------------------------------------------------------
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemCreateDto.quantity());
        orderItem.setPrice(product.getPrice());

        // -----------------------------------------------------------------
        // SALVA O ITEM DO PEDIDO NO BANCO
        // -----------------------------------------------------------------
        OrderItem saved = orderItemRepository.save(orderItem);

        // -----------------------------------------------------------------
        // RETORNA DTO DO ITEM SALVO
        // -----------------------------------------------------------------
        return toOrderItemDto(saved);
    }

    // ---------------------------------------------------------------------
    // RETORNA TODOS OS ITENS DE PEDIDO CADASTRADOS
    // ---------------------------------------------------------------------
    public List<OrderItemDto> findAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // RETORNA UM ITEM DE PEDIDO PELO ID
    // ---------------------------------------------------------------------
    public OrderItemDto findById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ORDER ITEM NOT FOUND"));
        return toOrderItemDto(orderItem);
    }

    // ---------------------------------------------------------------------
    // ATUALIZA UM ITEM DE PEDIDO EXISTENTE
    // ---------------------------------------------------------------------
    @Transactional
    public OrderItemDto updateOrderItem(Long id, OrderItemCreateDto orderItemCreateDto) {

        // BUSCA O ITEM DE PEDIDO PELO ID
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ORDER ITEM NOT FOUND"));

        // -----------------------------------------------------------------
        // ATUALIZA PRODUTO (CASO INFORMADO)
        // -----------------------------------------------------------------
        if (orderItemCreateDto.productId() != null) {
            Product product = productRepository.findById(orderItemCreateDto.productId())
                    .orElseThrow(() -> new RuntimeException("PRODUCT NOT FOUND"));
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
        }

        // -----------------------------------------------------------------
        // ATUALIZA QUANTIDADE (CASO INFORMADA)
        // -----------------------------------------------------------------
        if (orderItemCreateDto.quantity() != null) {
            orderItem.setQuantity(orderItemCreateDto.quantity());
        }

        // -----------------------------------------------------------------
        // SALVA ALTERAÇÕES
        // -----------------------------------------------------------------
        OrderItem updated = orderItemRepository.save(orderItem);

        // -----------------------------------------------------------------
        // RETORNA DTO ATUALIZADO
        // -----------------------------------------------------------------
        return toOrderItemDto(updated);
    }

    // ---------------------------------------------------------------------
    // DELETA UM ITEM DE PEDIDO PELO ID
    // ---------------------------------------------------------------------
    public void deleteOrderItem(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new RuntimeException("ORDER ITEM NOT FOUND");
        }
        orderItemRepository.deleteById(id);
    }

    // ---------------------------------------------------------------------
    // CONVERTE A ENTIDADE ORDERITEM PARA ORDERITEMDTO
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
