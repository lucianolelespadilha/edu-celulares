package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.entity.OrderItem;
import br.com.educelulares.backend.repository.OrderItemRepository;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderitems")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    // RETORNA TODOS OS ITENS DE PEDIDOS
    @GetMapping
    public ResponseEntity<?> findById() {
        return ResponseEntity.ok(orderItemRepository.findAll());
    }
    // RETORNA UM ITEM ESPECÍFICO PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> findById(@PathVariable Long id) {
        return orderItemRepository.findById(id)
                .map((ResponseEntity::ok))
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    @Transactional
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItem orderItem) {
        // VALIDA SE O PEDIDO FOI INFORMADO
        if(orderItem.getOrder() == null || orderItem.getOrder().getId() == null){
            return ResponseEntity.badRequest().body("Order ID required!");
        }
        // VALIDA SE O PRODUTO FOI INFORMADO
        if(orderItem.getProduct() == null || orderItem.getProduct().getId() == null){
            return ResponseEntity.badRequest().body("Product ID required!");
        }

    }

}
