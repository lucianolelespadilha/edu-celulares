package br.com.educelulares.backend.controller;


import br.com.educelulares.backend.dto.OrderCreateDto;
import br.com.educelulares.backend.dto.OrderDto;
import br.com.educelulares.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// -------------------------------------------------------------------------
// CONTROLADOR RESPONSÁVEL PELO GERENCIAMENTO DE PEDIDOS (ORDERS)
// PERMITE CRIAR, LISTAR, CONSULTAR E EXCLUIR PEDIDOS
// -------------------------------------------------------------------------
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    // INJEÇÃO AUTOMÁTICA DO SERVIÇO DE PEDIDOS
    private final OrderService orderService;

    // ---------------------------------------------------------------------
    // CRIA UM NOVO PEDIDO
    // ---------------------------------------------------------------------
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        // CRIA UM NOVO PEDIDO A PARTIR DO DTO E RETORNA HTTP 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderCreateDto));
    }

    // ---------------------------------------------------------------------
    // RETORNA TODOS OS PEDIDOS
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllOrders() {
        // RETORNA HTTP 200 (OK) COM A LISTA DE TODOS OS PEDIDOS CADASTRADOS
        return ResponseEntity.ok(orderService.findAll());
    }

    // ---------------------------------------------------------------------
    // RETORNA UM PEDIDO PELO ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        // PROCURA O PEDIDO PELO ID INFORMADO E RETORNA HTTP 200 (OK)
        return ResponseEntity.ok(orderService.findById(id));
    }

    // ---------------------------------------------------------------------
    // DELETA UM PEDIDO PELO ID
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        // REMOVE O PEDIDO CASO EXISTA E RETORNA HTTP 204 (NO CONTENT)
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
