package br.com.educelulares.backend.controller;


import br.com.educelulares.backend.entity.Customer;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.OrderItem;
import br.com.educelulares.backend.entity.Product;
import br.com.educelulares.backend.repository.CategoryRepository;
import br.com.educelulares.backend.repository.CustomerRepository;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.ProductRepository;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    //CRIA UM PEDIDO
    @PostMapping
    public ResponseEntity<Order>createOrder(@RequestBody Order order) {
        if(order.getCustomer()==null||order.getCustomer().getId()==null){
            return ResponseEntity.badRequest().build();
        }
        Optional<Customer> customer = customerRepository.findById(order.getCustomer().getId());
        if(customer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        order.setCustomer(customer.get());
        for (OrderItem item : order.getItems()){
            if(item.getProduct()==null||item.getProduct().getId()==null){
                return ResponseEntity.badRequest().build();
            }
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("PRODUCT NOT FOUND"));

            item.setProduct(product);
            item.setPrice(product.getPrice());

            item.setOrder(order);
        }
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    //PESQUISA TODOS OS PEDIDOS
    @GetMapping
    public ResponseEntity<?> getAllOrders(){
        return ResponseEntity.ok(orderRepository.findAll());
    }

    //PESQUISA O PEDIDO POR UMA ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //DELETA UM PEDIDO POR UMA ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id){
        if(!orderRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
