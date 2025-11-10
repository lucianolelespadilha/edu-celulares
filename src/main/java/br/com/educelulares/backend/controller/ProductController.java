package br.com.educelulares.backend.controller;


import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.OrderItem;
import br.com.educelulares.backend.entity.Product;
import br.com.educelulares.backend.repository.OrderItemRepository;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    // INJEÇÃO AUTOMÁTICA DOS REPOSITÓRIOS
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // -------------------------------------------------------------------------
    // RETORNA TODOS OS ITENS DE PEDIDO
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<?> findAll() {
        // RETORNA HTTP 200 (OK) COM A LISTA COMPLETA DE ITENS DE PEDIDO
        return ResponseEntity.ok(orderItemRepository.findAll());
    }

    // -------------------------------------------------------------------------
    // RETORNA UM ITEM DE PEDIDO PELO ID
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> findById(@PathVariable Long id) {
        // PROCURA O ITEM PELO ID INFORMADO E RETORNA 200 SE ENCONTRAR OU 404 SE NÃO
        return orderItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------------------------------------------------------------
    // CRIA UM NOVO ITEM DE PEDIDO
    // -------------------------------------------------------------------------
    @PostMapping
    @Transactional
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItem orderItem) {

        // VALIDA SE O JSON POSSUI UM PEDIDO (ORDER) COM ID
        if (orderItem.getOrder() == null || orderItem.getOrder().getId() == null) {
            // SE NÃO TIVER, RETORNA ERRO 400 - REQUISIÇÃO INVÁLIDA
            return ResponseEntity.badRequest().body("Order ID required!");
        }

        // VALIDA SE O JSON POSSUI UM PRODUTO (PRODUCT) COM ID
        if (orderItem.getProduct() == null || orderItem.getProduct().getId() == null) {
            // SE NÃO TIVER, RETORNA ERRO 400 - REQUISIÇÃO INVÁLIDA
            return ResponseEntity.badRequest().body("Product ID required!");
        }

        // PROCURA O PEDIDO (ORDER) PELO ID INFORMADO
        Optional<Order> orderOpt = orderRepository.findById(orderItem.getOrder().getId());
        if (orderOpt.isEmpty()) {
            // SE O PEDIDO NÃO EXISTIR, RETORNA 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found!");
        }

        // PROCURA O PRODUTO (PRODUCT) PELO ID INFORMADO
        Optional<Product> productOpt = productRepository.findById(orderItem.getProduct().getId());
        if (productOpt.isEmpty()) {
            // SE O PRODUTO NÃO EXISTIR, RETORNA 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        }

        // RELACIONA CORRETAMENTE O ITEM COM O PEDIDO E O PRODUTO
        orderItem.setOrder(orderOpt.get());
        orderItem.setProduct(productOpt.get());

        // SALVA O ITEM NO BANCO DE DADOS
        OrderItem savedItem = orderItemRepository.save(orderItem);

        // RETORNA 201 CREATED COM O ITEM SALVO
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    // -------------------------------------------------------------------------
    // DELETA UM ITEM DE PEDIDO PELO ID
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {

        // VERIFICA SE O ITEM EXISTE NO BANCO
        if (!orderItemRepository.existsById(id)) {
            // SE NÃO EXISTE, RETORNA 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }

        // SE EXISTE, DELETA O ITEM E RETORNA 204 NO CONTENT
        orderItemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
