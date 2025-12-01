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

/**
 * ============================================================================
 * SERVIÇO RESPONSÁVEL PELO FLUXO DE PAGAMENTO VIA PAGBANK (PIX).
 *
 * FUNÇÕES:
 *  1) CRIAR ORDEM PIX
 *  2) CONSULTAR STATUS
 *  3) PROCESSAR WEBHOOK DO PAGBANK
 *
 * TODA A COMUNICAÇÃO HTTP FICA NO PagBankClient.
 * ESTE SERVICE SOMENTE MONTA OS DTOs E ATUALIZA O DOMÍNIO.
 * ============================================================================
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PagBankService {

    private final OrderRepository orderRepository;
    private final PagBankClient pagBankClient;

    // =========================================================================
    // 1. CRIA UMA ORDEM PIX NO PAGBANK
    // =========================================================================
    @Transactional
    public PagBankOrderResponseDto createPaymentPix(Long orderId) {

        log.info("CRIANDO PAGAMENTO PIX PARA ORDER {}", orderId);

        // ---------------------------------------------------------------------
        // BUSCA A ORDEM NO BANCO
        // ---------------------------------------------------------------------
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        // ---------------------------------------------------------------------
        // VALIDAÇÃO DO CLIENTE
        // ---------------------------------------------------------------------
        Customer c = order.getCustomer();
        if (c == null) {
            throw new BadRequestException("A ordem não possui cliente associado.");
        }

        if (c.getCpf() == null || c.getCpf().isBlank()) {
            throw new BadRequestException("CPF/CNPJ do cliente é obrigatório para criar pagamento PIX.");
        }

        // ---------------------------------------------------------------------
        // MONTA ITENS PARA O PAGBANK
        // ---------------------------------------------------------------------
        List<PagBankItemDto> items = order.getItems().stream()
                .map(item -> new PagBankItemDto(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice().intValue()
                ))
                .toList();

        // ---------------------------------------------------------------------
        // CALCULA VALOR TOTAL DA ORDEM
        // ---------------------------------------------------------------------
        int totalAmount = order.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .intValue();

        // ---------------------------------------------------------------------
        // MONTA TELEFONE (AJUSTE CONFORME NECESSÁRIO)
        // ---------------------------------------------------------------------
        PagBankPhoneDto phone = new PagBankPhoneDto(
                "55",
                "11",
                "999999999"
        );

        // ---------------------------------------------------------------------
        // MONTA DADOS DO CLIENTE PARA ENVIO AO PAGBANK
        // ---------------------------------------------------------------------
        PagBankCustomerDto customerDto = new PagBankCustomerDto(
                c.getName(),
                c.getEmail(),
                c.getCpf(),
                phone
        );

        // ---------------------------------------------------------------------
        // ENDEREÇO DE ENTREGA
        // ---------------------------------------------------------------------
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

        // shipping.amount = frete
        PagBankShippingDto shipping = new PagBankShippingDto(address, 0);

        // ---------------------------------------------------------------------
        // URL DE NOTIFICAÇÃO PARA O WEBHOOK
        // ---------------------------------------------------------------------
        List<String> notifications = List.of(
                "https://seusite.com/webhook/pagbank"
        );

        // ---------------------------------------------------------------------
        // CHARGE DO PIX (OBRIGATÓRIA)
        // CONTÉM:
        //   amount.value = valor total
        //   payment_method.type = PIX
        // ---------------------------------------------------------------------
        PagBankChargeRequestDto charge = new PagBankChargeRequestDto(
                new PagBankChargeAmountRequestDto(totalAmount),
                new PagBankPaymentMethodRequestDto("PIX")
        );

        // ---------------------------------------------------------------------
        // MONTA DTO PRINCIPAL DA ORDEM
        // ---------------------------------------------------------------------
        PagBankOrderRequestDto requestDto = new PagBankOrderRequestDto(
                order.getId().toString(),
                customerDto,
                items,
                shipping,
                notifications,
                List.of(charge)
        );

        log.info("ENVIANDO ORDEM PIX AO PAGBANK...");

        // ---------------------------------------------------------------------
        // ENVIA ORDEM PARA O PAGBANK
        // ---------------------------------------------------------------------
        PagBankOrderResponseDto response = pagBankClient.createPixOrder(requestDto);

        log.info("PIX CRIADO COM SUCESSO: PAGBANK_ID={} ORDER_ID={} STATUS={}",
                response.getId(), order.getId(), response.getStatus());

        // ---------------------------------------------------------------------
        // CRIA PAYMENT LOCAL SE NÃO EXISTIR
        // ---------------------------------------------------------------------
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
    // 2. PROCESSA O WEBHOOK DO PAGBANK
    // =========================================================================
    @Transactional
    public void processWebhook(PagBankWebhookDto payload) {

        log.info("WEBHOOK RECEBIDO DO PAGBANK | reference_id={}  status={}",
                payload.getReferenceId(), payload.getStatus());

        Long orderId = Long.valueOf(payload.getReferenceId());

        // ---------------------------------------------------------------------
        // BUSCA ORDER
        // ---------------------------------------------------------------------
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

        // ---------------------------------------------------------------------
        // CONSULTA STATUS NO PAGBANK
        // ---------------------------------------------------------------------
        PagBankOrderResponseDto pg = pagBankClient.getPaymentStatus(payload.getId());

        String externalStatus = pg.getStatus() != null
                ? pg.getStatus().toUpperCase()
                : payload.getStatus().toUpperCase();

        log.info("STATUS ATUAL DO PAGBANK PARA ORDER {} = {}", orderId, externalStatus);

        // ---------------------------------------------------------------------
        // TRATAMENTO DOS STATUS
        // ---------------------------------------------------------------------
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
            default ->
                    log.warn("STATUS DO PAGBANK NÃO MAPEADO: {}", externalStatus);
        }

        orderRepository.save(order);

        log.info("PAYMENT ATUALIZADO COM SUCESSO | ORDER={} STATUS={}",
                orderId, payment.getStatus());
    }
}
