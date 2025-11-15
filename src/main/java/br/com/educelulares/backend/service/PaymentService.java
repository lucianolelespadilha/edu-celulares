package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.PaymentCreateDto;
import br.com.educelulares.backend.dto.PaymentDto;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.Payment;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // =========================================================================
    // CRIA UM NOVO PAGAMENTO
    // =========================================================================
    @Transactional
    public PaymentDto createPayment(PaymentCreateDto paymentCreateDto) {

        // ---------------------------------------------------------------------
        // VALIDA E BUSCA O PEDIDO REFERENCIADO PELO PAGAMENTO
        // ---------------------------------------------------------------------
        Order order = orderRepository.findById(paymentCreateDto.orderId())
                .orElseThrow(() ->
                        new IllegalArgumentException("ORDER NOT FOUND WITH ID: " + paymentCreateDto.orderId()));

        // ---------------------------------------------------------------------
        // GARANTE QUE O PEDIDO NÃO POSSUI PAGAMENTO DUPLICADO
        // ---------------------------------------------------------------------
        if (paymentRepository.existsByOrderId(paymentCreateDto.orderId())) {
            throw new IllegalArgumentException(
                    "PAYMENT ALREADY EXISTS FOR THIS ORDER: " + paymentCreateDto.orderId()
            );
        }

        // ---------------------------------------------------------------------
        // VALIDA VALOR DO PAGAMENTO
        // ---------------------------------------------------------------------
        if (paymentCreateDto.amount() == null ||
                paymentCreateDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "INVALID PAYMENT AMOUNT: " + paymentCreateDto.amount()
            );
        }

        // ---------------------------------------------------------------------
        // MONTA A ENTIDADE PAYMENT
        // ---------------------------------------------------------------------
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(paymentCreateDto.amount());

        // STATUS PADRÃO → "PENDING"
        payment.setStatus(
                paymentCreateDto.status() != null
                        ? paymentCreateDto.status()
                        : "PENDING"
        );

        // SE O STATUS FOR "PAID", REGISTRA O MOMENTO DO PAGAMENTO
        if ("PAID".equalsIgnoreCase(payment.getStatus())) {
            payment.setPaidAt(LocalDateTime.now());
        }

        // ---------------------------------------------------------------------
        // SALVA E RETORNA DTO
        // ---------------------------------------------------------------------
        Payment saved = paymentRepository.save(payment);
        return toPaymentDto(saved);
    }

    // =========================================================================
    // BUSCA PAGAMENTO POR ID
    // =========================================================================
    public PaymentDto findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("PAYMENT NOT FOUND WITH ID: " + id));

        return toPaymentDto(payment);
    }

    // =========================================================================
    // BUSCA TODOS OS PAGAMENTOS
    // =========================================================================
    public List<PaymentDto> findAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toPaymentDto)
                .toList();
    }

    // =========================================================================
    // ATUALIZA UM PAGAMENTO
    // =========================================================================
    @Transactional
    public PaymentDto updatePayment(Long id, PaymentCreateDto paymentCreateDto) {

        // ---------------------------------------------------------------------
        // BUSCA O PAGAMENTO QUE SERÁ ATUALIZADO
        // ---------------------------------------------------------------------
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("PAYMENT NOT FOUND WITH ID: " + id));

        // ---------------------------------------------------------------------
        // ATUALIZA O VALOR DO PAGAMENTO (SE INFORMADO)
        // ---------------------------------------------------------------------
        if (paymentCreateDto.amount() != null) {
            if (paymentCreateDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "INVALID PAYMENT AMOUNT: " + paymentCreateDto.amount()
                );
            }
            payment.setAmount(paymentCreateDto.amount());
        }

        // ---------------------------------------------------------------------
        // ATUALIZA O STATUS DO PAGAMENTO (SE INFORMADO)
        // ---------------------------------------------------------------------
        if (paymentCreateDto.status() != null) {

            String newStatus = paymentCreateDto.status().toUpperCase();

            switch (newStatus) {
                case "PAID" -> {
                    payment.setStatus("PAID");
                    payment.setPaidAt(LocalDateTime.now());
                }
                case "PENDING" -> {
                    payment.setStatus("PENDING");
                    payment.setPaidAt(null);
                }
                case "FAILED" -> {
                    payment.setStatus("FAILED");
                    payment.setPaidAt(null);
                }
                default -> throw new IllegalArgumentException("INVALID PAYMENT STATUS: " + newStatus);
            }
        }

        // ---------------------------------------------------------------------
        // SALVA ALTERAÇÕES E RETORNA DTO
        // ---------------------------------------------------------------------
        Payment updated = paymentRepository.save(payment);
        return toPaymentDto(updated);
    }

    // =========================================================================
    // REMOVE UM PAGAMENTO
    // =========================================================================
    @Transactional
    public void deletePayment(Long id) {

        // VALIDA EXISTÊNCIA DO PAGAMENTO
        if (!paymentRepository.existsById(id)) {
            throw new IllegalArgumentException("PAYMENT NOT FOUND WITH ID: " + id);
        }

        // REMOVE DO BANCO
        paymentRepository.deleteById(id);
    }

    // =========================================================================
    // CONVERTE ENTIDADE PARA DTO
    // =========================================================================
    private PaymentDto toPaymentDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaidAt()
        );
    }
}