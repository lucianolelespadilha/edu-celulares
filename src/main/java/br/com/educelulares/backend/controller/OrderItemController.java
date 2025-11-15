package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.dto.OrderItemCreateDto;
import br.com.educelulares.backend.dto.OrderItemDto;
import br.com.educelulares.backend.service.OrderItemService;
import br.com.educelulares.backend.service.OrderService;
import br.com.educelulares.backend.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderitems")
@RequiredArgsConstructor
public class OrderItemController {

    // INJEÇÃO AUTOMÁTICA DOS SERVIÇOS
    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final ProductService productService;

    // -------------------------------------------------------------------------
    // CRIA UM NOVO ITEM DE PEDIDO
    // -------------------------------------------------------------------------
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<OrderItemDto> createOrderItem(@RequestBody OrderItemCreateDto orderItemCreateDto) {

        // DELEGA A CRIAÇÃO PARA O SERVICE E RETORNA HTTP 201 (CREATED)
        OrderItemDto created = orderItemService.createOrderItem(orderItemCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // -------------------------------------------------------------------------
    // RETORNA TODOS OS ITENS DE PEDIDO
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<OrderItemDto>> findAll() {

        // RETORNA HTTP 200 (OK) COM A LISTA COMPLETA DE ITENS DE PEDIDO (DTO)
        return ResponseEntity.ok(orderItemService.findAllOrderItems());
    }

    // -------------------------------------------------------------------------
    // RETORNA UM ITEM DE PEDIDO PELO ID
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> findById(@PathVariable Long id) {

        // BUSCA O ITEM PELO ID E RETORNA COMO DTO (SERVICE LANÇA EXCEÇÃO SE NÃO EXISTIR)
        return ResponseEntity.ok(orderItemService.findById(id));
    }

    // -------------------------------------------------------------------------
    // ATUALIZA UM ITEM DE PEDIDO EXISTENTE
    // -------------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> updateOrderItem(
            @PathVariable Long id,
            @RequestBody OrderItemCreateDto orderItemCreateDto) {

        // DELEGA AO SERVICE A ATUALIZAÇÃO DO ITEM E RETORNA LISTA ATUALIZADA
        return ResponseEntity.ok(orderItemService.updateOrderItem(id, orderItemCreateDto));
    }

    // -------------------------------------------------------------------------
    // DELETA UM ITEM DE PEDIDO PELO ID
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderItemDto> deleteOrderItem(@PathVariable Long id) {

        // DELEGA A EXCLUSÃO AO SERVICE E RETORNA 204 (NO CONTENT)
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
