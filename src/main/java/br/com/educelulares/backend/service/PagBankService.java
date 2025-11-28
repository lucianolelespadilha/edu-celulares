package br.com.educelulares.backend.service;

import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.Payment;
import br.com.educelulares.backend.enums.PaymentStatus;
import br.com.educelulares.backend.exception.NotFoundException;
import br.com.educelulares.backend.pagbank.PagBankClient;
import br.com.educelulares.backend.dto.PagBankAddressDto;
import br.com.educelulares.backend.dto.PagBankCustomerDto;
import br.com.educelulares.backend.dto.PagBankItemDto;
import br.com.educelulares.backend.dto.PagBankOrderRequestDto;
import br.com.educelulares.backend.dto.PagBankOrderResponseDto;
import br.com.educelulares.backend.dto.PagBankPhoneDto;
import br.com.educelulares.backend.dto.PagBankShippingDto;
import br.com.educelulares.backend.dto.PagBankWebhookDto;
import br.com.educelulares.backend.repository.OrderRepository;
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


    // =============================================================================
    // 1. CRIA UMA ORDEM DE PAGAMENTO PIX NO PAGBANK
    // =============================================================================
    public PagBankOrderResponseDto createPaymentPix(Long orderId) {

        log.info("CRIANDO PAGAMENTO PIX PARA ORDER {}", orderId);

        // Busca a order no banco
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found " + orderId));

        // Converte itens do pedido para DTO do PagBank
        List<PagBankItemDto> items = order.getItems().stream()
                .map(item -> new PagBankItemDto(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice().intValue()
                ))
                .toList();

        // Calcula total em centavos
        int totalAmount = order.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .intValue();

        // Cliente (mock)
        PagBankCustomerDto customer = new PagBankCustomerDto(
                "Test Client",
                "email@test.com",
                new PagBankPhoneDto("55", "11", "999999999")
        );

        // Endereço (mock)
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

        // Shipping exigido pelo PagBank
        PagBankShippingDto shipping = new PagBankShippingDto(address, totalAmount);

        // Webhook
        List<String> notificationUrls = List.of("https://seusite.com/webhook/pagbank");

        // DTO final da requisição
        PagBankOrderRequestDto requestDto = new PagBankOrderRequestDto(
                order.getId().toString(),
                customer,
                items,
                shipping,
                notificationUrls
        );

        // Chamada correta do PagBankClient
        PagBankOrderResponseDto responseDto = pagBankClient.createPixOrder(requestDto);

        log.info("PAGBANK GEROU PIX ORDER INTERNA={} PAGBANK_ID={}",
                order.getId(), responseDto.getId());

        if(order.getPayment() == null){
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setStatus(PaymentStatus.PENDING);
            order.setPayment(payment);
        }

        return responseDto;
    }



    // =============================================================================
    // 2. PROCESSA O WEBHOOK DO PAGBANK
    // =============================================================================
    public void processWebhook(PagBankWebhookDto payload) {

        log.info("WEBHOOK RECEBIDO | reference_id={} status={}",
                payload.getReferenceId(), payload.getStatus());

        // Busca order interna
        Order order = orderRepository.findById(Long.valueOf(payload.getReferenceId()))
                .orElseThrow(() ->
                        new NotFoundException("Order not found " + payload.getReferenceId()));

        // Recupera ou cria o Payment associado
        Payment payment = order.getPayment();
        if (payment == null) {
            payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            order.setPayment(payment);
        }

        // Consulta status atualizado no PagBank
        PagBankOrderResponseDto pagBankResponse =
                pagBankClient.getPaymentStatus(payload.getId());

        String externalStatus = pagBankResponse.getStatus() != null
                ? pagBankResponse.getStatus().toUpperCase()
                : (payload.getStatus() != null ? payload.getStatus().toUpperCase() : "UNKNOWN");

        log.info("STATUS DO PAGBANK PARA ORDER {} = {}", order.getId(), externalStatus);


        // Mapeamento de status
        switch (externalStatus) {
            case "PAID" -> {
                payment.setStatus(PaymentStatus.PAID);
                payment.setPaidAt(LocalDateTime.now());
                log.info("PAGAMENTO CONFIRMADO PARA ORDER {}", order.getId());
            }
            case "WAITING_PAYMENT", "CREATED" -> {
                payment.setStatus(PaymentStatus.PENDING);
                log.info("PAGAMENTO PENDENTE PARA ORDER {}", order.getId());
            }
            case "EXPIRED" -> {
                payment.setStatus(PaymentStatus.EXPIRED);
                log.warn("PAGAMENTO EXPIRADO PARA ORDER {}", order.getId());
            }
            case "CANCELED", "REFUSED" -> {
                payment.setStatus(PaymentStatus.FAILED);
                log.warn("PAGAMENTO CANCELADO/RECUSADO PARA ORDER {}", order.getId());
            }
            default -> log.warn("STATUS NÃO MAPEADO: {}", externalStatus);
        }

        // Salva order + payment (Cascade.ALL)
        orderRepository.save(order);

        log.info("PAYMENT ATUALIZADO | order={} status={}",
                order.getId(), payment.getStatus());
    }

}
