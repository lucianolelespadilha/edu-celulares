package br.com.educelulares.backend.service.pagbank;

import br.com.educelulares.backend.client.PagBankClient;
import br.com.educelulares.backend.dto.pagbank.*;
import br.com.educelulares.backend.entity.Customer;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.entity.Payment;
import br.com.educelulares.backend.enums.PaymentStatus;
import br.com.educelulares.backend.exception.BadRequestException;
import br.com.educelulares.backend.exception.NotFoundException;
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

    // =====================================================================
    // 1) CRIA O PEDIDO PIX NO PAGBANK
    // =====================================================================
    @Transactional
    public PagBankOrderResponseDto createPaymentPix(Long orderId) {

        log.info("=========== CRIANDO PAGAMENTO PIX PARA ORDER {} ==========", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        Customer c = order.getCustomer();
        if (c == null)
            throw new BadRequestException("A ordem não possui cliente.");

        if (c.getCpf() == null || c.getCpf().isBlank())
            throw new BadRequestException("CPF do cliente é obrigatório para PIX.");

        int total = order.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(100))
                .intValue();

        List<PagBankItemDto> items = order.getItems().stream()
                .map(i -> new PagBankItemDto(
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice().multiply(BigDecimal.valueOf(100)).intValue()
                ))
                .toList();

        PagBankPhoneDto phone = new PagBankPhoneDto("55", "11", "999999999", "MOBILE");

        PagBankCustomerDto customerDto = new PagBankCustomerDto(
                c.getName(), c.getEmail(), c.getCpf(), List.of(phone)
        );

        PagBankAddressDto address = new PagBankAddressDto(
                "Rua Teste", "123", "Casa",
                "Bairro Teste", "São Paulo", "SP",
                "BRA", "01001000"
        );

        PagBankShippingDto shipping = new PagBankShippingDto(
                address, new PagBankShippingAmountDto(0)
        );

        PagBankQrCodeRequestDto qrCode = new PagBankQrCodeRequestDto(
                new PagBankChargeAmountRequestDto(total), null
        );

        PagBankOrderRequestDto req = new PagBankOrderRequestDto(
                order.getId().toString(),
                customerDto,
                items,
                List.of(qrCode),
                shipping,
                List.of("https://seusite.com/webhook/pagbank")
        );

        PagBankOrderResponseDto resp = pagBankClient.createPixOrder(req);

        if (resp == null)
            throw new BadRequestException("Erro ao criar pedido PIX no PagBank.");

        order.setPagbankOrderId(resp.getId());

        Payment payment = order.getPayment();
        if (payment == null) {
            payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setStatus(PaymentStatus.PENDING);
            order.setPayment(payment);
        }

        orderRepository.save(order);

        return resp;
    }

    // =====================================================================
    // 2) PROCESSA WEBHOOK
    // =====================================================================
    @Transactional
    public void processWebhook(PagBankWebhookDto webhook) {

        Long orderId = Long.valueOf(webhook.getReferenceId());

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

        PagBankOrderResponseDto response =
                pagBankClient.getOrderStatus(webhook.getId());

        String status = determinePixPaymentStatus(response);

        applyStatus(payment, status);

        orderRepository.save(order);

        log.info("[WEBHOOK] ORDER {} ATUALIZADA PARA {}", orderId, payment.getStatus());
    }

    // =====================================================================
    // 3) SINCRONIZA STATUS MANUALMENTE
    // =====================================================================
    @Transactional
    public String syncPaymentStatus(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        if (order.getPagbankOrderId() == null)
            throw new BadRequestException("Esta ordem não possui pagbankOrderId.");

        PagBankOrderResponseDto response =
                pagBankClient.getOrderStatus(order.getPagbankOrderId());

        String status = determinePixPaymentStatus(response);

        Payment payment = order.getPayment();
        if (payment == null) {
            payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            order.setPayment(payment);
        }

        applyStatus(payment, status);

        orderRepository.save(order);

        return payment.getStatus().name();
    }

    // =====================================================================
    // MÉTODOS AUXILIARES
    // =====================================================================
    private String determinePixPaymentStatus(PagBankOrderResponseDto response) {
        // Verifica se o status de pagamento está em charges
        if (response.getCharges() != null && !response.getCharges().isEmpty()) {
            String chargeStatus = response.getCharges().get(0).getStatus();  // 'get(0)' para pegar o primeiro charge
            if (chargeStatus != null && !chargeStatus.isBlank()) {
                return chargeStatus.toUpperCase();  // Retorna o status do charge
            }
        }

        // Caso não tenha status em charges, verifica no status principal
        if (response.getStatus() != null && !response.getStatus().isBlank()) {
            return response.getStatus().toUpperCase();  // Retorna o status da ordem
        }

        return "UNKNOWN";  // Retorna "UNKNOWN" se não encontrar status
    }



    private void applyStatus(Payment payment, String status) {
        switch (status) {
            case "PAID" -> {
                payment.setStatus(PaymentStatus.PAID);
                payment.setPaidAt(LocalDateTime.now());
            }
            case "WAITING", "WAITING_PAYMENT", "CREATED" -> {
                payment.setStatus(PaymentStatus.PENDING);
                payment.setPaidAt(null);
            }
            case "EXPIRED" -> payment.setStatus(PaymentStatus.EXPIRED);
            case "CANCELED", "REFUNDED" -> payment.setStatus(PaymentStatus.FAILED);
            default -> {
                log.warn("Status PagBank não mapeado: {}", status);
                payment.setStatus(PaymentStatus.PENDING);
            }
        }
    }
}
