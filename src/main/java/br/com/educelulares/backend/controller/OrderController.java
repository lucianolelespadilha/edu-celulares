package br.com.educelulares.backend.controller;


import br.com.educelulares.backend.entity.Customer;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.OrderItem;
import br.com.educelulares.backend.entity.Product;
import br.com.educelulares.backend.repository.CustomerRepository;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// -------------------------------------------------------------------------
// CONTROLADOR RESPONSÁVEL PELO GERENCIAMENTO DE PEDIDOS (ORDERS)
// PERMITE CRIAR, LISTAR, CONSULTAR E EXCLUIR PEDIDOS
// -------------------------------------------------------------------------

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    // INJEÇÃO AUTOMÁTICA DOS REPOSITÓRIOS
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    // ---------------------------------------------------------------------
    // CRIA UM NOVO PEDIDO
    // ---------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {

        // VALIDA SE O PEDIDO POSSUI UM CLIENTE (CUSTOMER) COM ID
        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            // RETORNA ERRO 400 CASO O CLIENTE NÃO TENHA SIDO INFORMADO
            return ResponseEntity.badRequest().build();
        }

        // PROCURA O CLIENTE NO BANCO PELO ID INFORMADO
        Optional<Customer> customer = customerRepository.findById(order.getCustomer().getId());
        if (customer.isEmpty()) {
            // RETORNA ERRO 404 CASO O CLIENTE NÃO EXISTA
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // DEFINE O CLIENTE ENCONTRADO NO PEDIDO
        order.setCustomer(customer.get());

        // PERCORRE TODOS OS ITENS DO PEDIDO
        for (OrderItem item : order.getItems()) {

            // VALIDA SE CADA ITEM POSSUI UM PRODUTO COM ID
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                // SE NÃO, RETORNA ERRO 400 (REQUISIÇÃO INVÁLIDA)
                return ResponseEntity.badRequest().build();
            }

            // PROCURA O PRODUTO NO BANCO
            Product product = productRepository.findById(item.getProduct().getId())
                    // SE NÃO ENCONTRAR, LANÇA UMA EXCEÇÃO
                    .orElseThrow(() -> new RuntimeException("PRODUCT NOT FOUND"));

            // DEFINE O PRODUTO E O PREÇO NO ITEM
            item.setProduct(product);
            item.setPrice(product.getPrice());

            // RELACIONA O ITEM AO PEDIDO ATUAL
            item.setOrder(order);
        }

        // SALVA O PEDIDO COMPLETO (COM ITENS E CLIENTE)
        Order savedOrder = orderRepository.save(order);

        // RETORNA HTTP 201 (CREATED) COM O PEDIDO SALVO
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    // ---------------------------------------------------------------------
    // RETORNA TODOS OS PEDIDOS
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        // RETORNA HTTP 200 (OK) COM A LISTA DE TODOS OS PEDIDOS CADASTRADOS
        return ResponseEntity.ok(orderRepository.findAll());
    }

    // ---------------------------------------------------------------------
    // RETORNA UM PEDIDO PELO ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        // PROCURA O PEDIDO NO BANCO PELO ID INFORMADO
        return orderRepository.findById(id)
                // RETORNA 200 SE ENCONTRAR OU 404 SE NÃO EXISTIR
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------------
    // DELETA UM PEDIDO PELO ID
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {

        // VERIFICA SE O PEDIDO EXISTE
        if (!orderRepository.existsById(id)) {
            // SE NÃO EXISTIR, RETORNA 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }

        // REMOVE O PEDIDO DO BANCO
        orderRepository.deleteById(id);

        // RETORNA HTTP 204 (NO CONTENT) INDICANDO SUCESSO SEM CONTEÚDO
        return ResponseEntity.noContent().build();
    }
}