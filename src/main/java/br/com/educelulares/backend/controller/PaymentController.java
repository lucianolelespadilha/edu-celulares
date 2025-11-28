package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.dto.PaymentCreateDto;
import br.com.educelulares.backend.dto.PaymentDto;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.Payment;
import br.com.educelulares.backend.service.OrderService;
import br.com.educelulares.backend.service.PaymentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

    // -------------------------------------------------------------------------
    // CONTROLADOR RESPONSÁVEL PELO GERENCIAMENTO DE PAGAMENTOS (PAYMENTS)
    // PERMITE CRIAR, LISTAR, CONSULTAR, ATUALIZAR E EXCLUIR PAGAMENTOS
    // -------------------------------------------------------------------------
    @RestController
    @RequestMapping("/payments")
    @RequiredArgsConstructor
    public class PaymentController {
    
        // INJEÇÃO AUTOMÁTICA DO SERVICE DE PAGAMENTOS
        private final PaymentService paymentService;
    
    
        // ---------------------------------------------------------------------
        // CRIA UM NOVO PAGAMENTO ASSOCIADO A UM PEDIDO EXISTENTE
        // ---------------------------------------------------------------------
        @PostMapping
        @Transactional
        public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentCreateDto paymentCreateDto) {
    
            // DELEGA A LÓGICA DE CRIAÇÃO AO SERVICE E RETORNA 201 (CREATED)
            PaymentDto created = paymentService.createPayment(paymentCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
    
    
        // ---------------------------------------------------------------------
        // LISTA TODOS OS PAGAMENTOS CADASTRADOS
        // ---------------------------------------------------------------------
        @GetMapping
        public ResponseEntity<List<PaymentDto>> findAll() {
    
            // RETORNA HTTP 200 (OK) COM A LISTA COMPLETA DE PAGAMENTOS
            return ResponseEntity.ok(paymentService.findAll());
        }
    
    
        // ---------------------------------------------------------------------
        // BUSCA UM PAGAMENTO PELO ID
        // ---------------------------------------------------------------------
        @GetMapping("/{id}")
        public ResponseEntity<PaymentDto> findById(@PathVariable Long id) {
    
            // PROCURA O PAGAMENTO PELO ID E RETORNA 200 SE ENCONTRAR OU 404 SE NÃO EXISTIR
            return ResponseEntity.ok(paymentService.findById(id));
        }
    
    
        // ---------------------------------------------------------------------
        // ATUALIZA UM PAGAMENTO EXISTENTE
        // ---------------------------------------------------------------------
        @PutMapping("/{id}")
        @Transactional
        public ResponseEntity<PaymentDto> updatePayment(
                @PathVariable Long id,
                @Valid @RequestBody PaymentCreateDto paymentCreateDto) {
    
            // ATUALIZA O PAGAMENTO E RETORNA 200 (OK)
            PaymentDto updated = paymentService.updatePayment(id, paymentCreateDto);
            return ResponseEntity.ok(updated);
        }
    
    
        // ---------------------------------------------------------------------
        // EXCLUI UM PAGAMENTO PELO ID
        // ---------------------------------------------------------------------
        @DeleteMapping("/{id}")
        @Transactional
        public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
    
            // REMOVE O PAGAMENTO E RETORNA 204 (NO CONTENT)
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        }
    }
