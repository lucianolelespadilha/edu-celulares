package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.PaymentCreateDto;
import br.com.educelulares.backend.dto.PaymentDto;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.Payment;
import br.com.educelulares.backend.enums.PaymentStatus;
import br.com.educelulares.backend.exception.ConflictException;
import br.com.educelulares.backend.repository.OrderRepository;
import br.com.educelulares.backend.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;


    // =========================================================================
    // CREATE PAYMENT
    // =========================================================================
    @Transactional
    public PaymentDto createPayment(PaymentCreateDto dto) {

        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new ConflictException("ORDER NOT FOUND: " + dto.orderId()));

        if (paymentRepository.existsByOrderId(dto.orderId())) {
            throw new ConflictException("PAYMENT ALREADY EXISTS FOR ORDER: " + dto.orderId());
        }

        if (dto.amount() == null || dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("INVALID PAYMENT AMOUNT: " + dto.amount());
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(dto.amount());

        // Se vier null → vira PENDING
        PaymentStatus status = dto.status() != null
                ? dto.status()
                : PaymentStatus.PENDING;

        payment.setStatus(status);

        if (status == PaymentStatus.PAID) {
            payment.setPaidAt(LocalDateTime.now());
        }

        Payment saved = paymentRepository.save(payment);
        return toPaymentDto(saved);
    }


    // =========================================================================
    // GET BY ID
    // =========================================================================
    public PaymentDto findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ConflictException("PAYMENT NOT FOUND: " + id));
        return toPaymentDto(payment);
    }


    // =========================================================================
    // LIST ALL
    // =========================================================================
    public List<PaymentDto> findAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toPaymentDto)
                .toList();
    }


    // =========================================================================
    // UPDATE PAYMENT
    // =========================================================================
    @Transactional
    public PaymentDto updatePayment(Long id, PaymentCreateDto dto) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ConflictException("PAYMENT NOT FOUND: " + id));

        // Atualiza amount, se enviado
        if (dto.amount() != null) {
            if (dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ConflictException("INVALID PAYMENT AMOUNT: " + dto.amount());
            }
            payment.setAmount(dto.amount());
        }

        // Atualiza status, se enviado
        if (dto.status() != null) {

            PaymentStatus statusEnum = dto.status();

            switch (statusEnum) {
                case PAID -> {
                    payment.setStatus(PaymentStatus.PAID);
                    payment.setPaidAt(LocalDateTime.now());
                }
                case PENDING -> {
                    payment.setStatus(PaymentStatus.PENDING);
                    payment.setPaidAt(null);
                }
                case FAILED -> {
                    payment.setStatus(PaymentStatus.FAILED);
                    payment.setPaidAt(null);
                }
                case EXPIRED -> {
                    payment.setStatus(PaymentStatus.EXPIRED);
                    payment.setPaidAt(null);
                }
                default -> throw new ConflictException("INVALID PAYMENT STATUS: " + statusEnum);
            }
        }

        Payment updated = paymentRepository.save(payment);
        return toPaymentDto(updated);
    }


    // =========================================================================
    // DELETE
    // =========================================================================
    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ConflictException("PAYMENT NOT FOUND: " + id);
        }
        paymentRepository.deleteById(id);
    }


    // =========================================================================
    // MAPPER
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
