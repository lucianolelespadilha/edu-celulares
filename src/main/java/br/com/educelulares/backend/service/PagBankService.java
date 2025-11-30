package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.*;
import br.com.educelulares.backend.entity.Customer;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.Payment;
import br.com.educelulares.backend.enums.PaymentStatus;
import br.com.educelulares.backend.exception.BadRequestException;
import br.com.educelulares.backend.exception.NotFoundException;
import br.com.educelulares.backend.pagbank.PagBankClient;
import br.com.educelulares.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagBankService {

    private final OrderRepository orderRepository;
    private final PagBankClient pagBankClient;

    // =========================================================================
    // 1. CRIA ORDEM PIX NO PAGBANK
    // =========================================================================
    @Transactional
    public PagBankOrderResponseDto createPaymentPix(Long orderId) {

        log.info("CRIANDO PAGAMENTO PIX PARA ORDER {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        // ====================================================
        // VALIDAÇÃO DO CLIENTE
        // ====================================================
        Customer c = order.getCustomer();
        if (c == null) {
            throw new BadRequestException("A ordem não possui cliente associado.");
        }

        if (c.getCpf() == null || c.getCpf().isBlank()) {
            throw new BadRequestException("CPF/CNPJ do cliente é obrigatório para criar pagamento PIX.");
        }

        // ====================================================
        // MONTA ITENS
        // ====================================================
        List<PagBankItemDto> items = order.getItems().stream()
                .map(item -> new PagBankItemDto(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice().intValue()
                ))
                .toList();

        int totalAmount = order.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .intValue();

        // ====================================================
        // MONTA TELEFONE DO CLIENTE (usar dados reais se existirem)
        // ====================================================
        PagBankPhoneDto phone = new PagBankPhoneDto(
                "55",
                "11",
                "999999999"
        );

        // ====================================================
        // MONTA CLIENTE PARA O PAGBANK (cpf vira tax_id no JSON)
        // ====================================================
        PagBankCustomerDto customerDto = new PagBankCustomerDto(
                c.getName(),
                c.getEmail(),
                c.getCpf(),
                phone
        );

        // ====================================================
        // ENDEREÇO E FRETE (fixo ou via Order)
        // ====================================================
        PagBankAddressDto address = new PagBankAddressDto(
                "Test Street",
                "123",
                "House",
                "District",
                "São Paulo",
                "SP",
                "SP",
                "BRA",
                "01001000"
        );

        PagBankShippingDto shipping = new PagBankShippingDto(address, totalAmount);

        // ====================================================
        // NOTIFICATION WEBHOOK
        // ====================================================
        List<String> notifications = List.of(
                "https://seusite.com/webhook/pagbank"
        );

        // ====================================================
        // MONTAGEM DO REQUEST AO PAGBANK
        // ====================================================
        PagBankOrderRequestDto requestDto = new PagBankOrderRequestDto(
                order.getId().toString(),
                customerDto,
                items,
                shipping,
                notifications
        );

        // ====================================================
        // ENVIA REQUISIÇÃO PARA O PAGBANK
        // ====================================================
        PagBankOrderResponseDto response = pagBankClient.createPixOrder(requestDto);

        log.info("PIX GERADO PARA ORDER={} PAGBANK_ID={}", order.getId(), response.getId());

        // ====================================================
        // CRIA O REGISTRO DE PAYMENT SE NÃO EXISTIR
        // ====================================================
        if (order.getPayment() == null) {
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setStatus(PaymentStatus.PENDING);
            order.setPayment(payment);
        }

        orderRepository.save(order);

        return response;
    }

    // =========================================================================
    // 2. PROCESSA WEBHOOK PAGBANK
    // =========================================================================
    @Transactional
    public void processWebhook(PagBankWebhookDto payload) {

        log.info("WEBHOOK RECEBIDO | reference_id={} status={}",
                payload.getReferenceId(), payload.getStatus());

        Long orderId = Long.valueOf(payload.getReferenceId());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        Payment payment = order.getPayment();
        if (payment == null) {
            payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setStatus(PaymentStatus.PENDING);
            order.setPayment(payment);
        }

        // Consulta status atualizado no PagBank
        PagBankOrderResponseDto pg = pagBankClient.getPaymentStatus(payload.getId());

        String externalStatus = pg.getStatus() != null
                ? pg.getStatus().toUpperCase()
                : payload.getStatus().toUpperCase();

        log.info("STATUS DO PAGBANK PARA ORDER {} = {}", orderId, externalStatus);

        switch (externalStatus) {
            case "PAID" -> {
                payment.setStatus(PaymentStatus.PAID);
                payment.setPaidAt(LocalDateTime.now());
            }
            case "WAITING_PAYMENT", "CREATED" -> {
                payment.setStatus(PaymentStatus.PENDING);
                payment.setPaidAt(null);
            }
            case "EXPIRED" -> {
                payment.setStatus(PaymentStatus.EXPIRED);
                payment.setPaidAt(null);
            }
            case "CANCELED", "REFUSED" -> {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setPaidAt(null);
            }
            default -> log.warn("STATUS NÃO MAPEADO: {}", externalStatus);
        }

        orderRepository.save(order);

        log.info("PAYMENT ATUALIZADO | order={} status={}", orderId, payment.getStatus());
    }
}
