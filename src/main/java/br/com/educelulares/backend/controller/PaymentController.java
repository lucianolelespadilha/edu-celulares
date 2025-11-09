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

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    // INJEÇÃO AUTOMÁTICA DOS REPOSITÓRIOS
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // -------------------------------------------------------------------------
    // BUSCA TODOS OS PAGAMENTOS
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<?> findAll() {
        // RETORNA HTTP 200 COM A LISTA COMPLETA DE PAGAMENTOS
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    // -------------------------------------------------------------------------
    // BUSCA UM PAGAMENTO PELO ID
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        // PROCURA O PAGAMENTO NO BANCO E RETORNA 200 SE ENCONTRAR OU 404 SE NÃO
        return paymentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------------------------------------------------------------
    // CRIA UM NOVO PAGAMENTO
    // -------------------------------------------------------------------------
    @PostMapping
    @Transactional
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {

        // VALIDA SE O JSON ENVIADO TEM UM PEDIDO (ORDER) COM ID
        if (payment.getOrder() == null || payment.getOrder().getId() == null) {
            // SE NÃO TIVER, RETORNA ERRO 400 - REQUISIÇÃO INVÁLIDA
            return ResponseEntity.badRequest().build();
        }

        // PROCURA O PEDIDO (ORDER) PELO ID INFORMADO NO JSON
        Optional<Order> orderOpt = orderRepository.findById(payment.getOrder().getId());

        // SE O PEDIDO NÃO EXISTIR NO BANCO, RETORNA 404
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // O PEDIDO EXISTE, ENTÃO PEGAMOS O OBJETO
        Order order = orderOpt.get();

        // RELACIONAMENTO BIDIRECIONAL: PAGAMENTO CONHECE O PEDIDO E VICE-VERSA
        payment.setOrder(order);
        order.setPayment(payment);

        // SALVA O PEDIDO. COMO ORDER TEM CASCADE = ALL, O PAYMENT SERÁ SALVO JUNTO
        Order savedOrder = orderRepository.save(order);

        // OBTÉM O PAYMENT SALVO DO ORDER RETORNADO PARA TER O ID GERADO
        Payment savedPayment = savedOrder.getPayment();

        // RETORNA 201 CREATED COM O PAYMENT JÁ PERSISTIDO
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
    }

    // -------------------------------------------------------------------------
    // DELETA UM PAGAMENTO PELO ID
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {

        // VERIFICA SE O PAGAMENTO EXISTE NO BANCO
        if (!paymentRepository.existsById(id)) {
            // SE NÃO EXISTE, RETORNA 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }

        // SE EXISTE, DELETA E RETORNA 204 NO CONTENT
        paymentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
