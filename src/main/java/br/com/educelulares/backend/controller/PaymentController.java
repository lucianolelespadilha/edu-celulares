package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.Payment;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// -------------------------------------------------------------------------
// CONTROLADOR RESPONSÁVEL PELO GERENCIAMENTO DE PAGAMENTOS (PAYMENTS)
// PERMITE CRIAR, LISTAR, CONSULTAR E EXCLUIR PAGAMENTOS ASSOCIADOS A PEDIDOS
// -------------------------------------------------------------------------
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    // INJEÇÃO AUTOMÁTICA DOS REPOSITÓRIOS
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // ---------------------------------------------------------------------
    // LISTA TODOS OS PAGAMENTOS CADASTRADOS
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<?> findAll() {
        // RETORNA HTTP 200 (OK) COM A LISTA COMPLETA DE PAGAMENTOS
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    // ---------------------------------------------------------------------
    // BUSCA UM PAGAMENTO PELO ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        // PROCURA O PAGAMENTO PELO ID E RETORNA 200 SE ENCONTRAR OU 404 SE NÃO EXISTIR
        return paymentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------------
    // CRIA UM NOVO PAGAMENTO ASSOCIADO A UM PEDIDO EXISTENTE
    // ---------------------------------------------------------------------
    @PostMapping
    @Transactional
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {

        // VERIFICA SE O JSON INFORMOU UM PEDIDO COM ID
        if (payment.getOrder() == null || payment.getOrder().getId() == null) {
            // SE NÃO INFORMOU, RETORNA ERRO 400 (BAD REQUEST)
            return ResponseEntity.badRequest().build();
        }

        // BUSCA O PEDIDO REFERENCIADO PELO ID INFORMADO
        Optional<Order> orderOpt = orderRepository.findById(payment.getOrder().getId());

        // SE O PEDIDO NÃO EXISTIR, RETORNA ERRO 404 (NOT FOUND)
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // O PEDIDO EXISTE, ENTÃO OBTÉM O OBJETO
        Order order = orderOpt.get();

        // DEFINE O RELACIONAMENTO BIDIRECIONAL ENTRE ORDER E PAYMENT
        payment.setOrder(order);
        order.setPayment(payment);

        // SALVA O PEDIDO; O PAGAMENTO SERÁ PERSISTIDO AUTOMATICAMENTE (CASCADE = ALL)
        Order savedOrder = orderRepository.save(order);

        // OBTÉM O PAGAMENTO JÁ SALVO (COM ID GERADO)
        Payment savedPayment = savedOrder.getPayment();

        // RETORNA 201 (CREATED) COM O OBJETO PAYMENT PERSISTIDO
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
    }

    // ---------------------------------------------------------------------
    // EXCLUI UM PAGAMENTO PELO ID
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {

        // VERIFICA SE O PAGAMENTO EXISTE NO BANCO
        if (!paymentRepository.existsById(id)) {
            // SE NÃO EXISTIR, RETORNA 404 (NOT FOUND)
            return ResponseEntity.notFound().build();
        }

        // REMOVE O PAGAMENTO E RETORNA 204 (NO CONTENT) INDICANDO SUCESSO
        paymentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
