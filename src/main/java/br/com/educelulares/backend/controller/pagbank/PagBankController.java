package br.com.educelulares.backend.controller.pagbank;

import br.com.educelulares.backend.dto.pagbank.PagBankOrderResponseDto;
import br.com.educelulares.backend.dto.pagbank.PagBankWebhookDto;
import br.com.educelulares.backend.service.pagbank.PagBankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================================
 * CONTROLLER RESPONSÁVEL POR RECEBER REQUISIÇÕES RELACIONADAS AO PAGBANK.
 *
 * ENDPOINTS:
 *
 *  1) POST /api/pagbank/orders/{id}
 *     → CRIA UM PEDIDO PIX COM QR CODE
 *
 *  2) POST /api/pagbank/webhook
 *     → RECEBE NOTIFICAÇÕES AUTOMÁTICAS DO PAGBANK
 *
 * TODA A REGRA DE NEGÓCIO ESTÁ EM PagBankService.
 * ============================================================================
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pagbank")
public class PagBankController {

    private final PagBankService pagBankService;

    // =========================================================================
    // 1. CRIAR PEDIDO PIX (QR CODE)
    // =========================================================================
    @PostMapping("/orders/{orderId}")
    public ResponseEntity<PagBankOrderResponseDto> createPix(
            @PathVariable Long orderId) {

        log.info("[CONTROLLER] REQUISIÇÃO PARA CRIAR PIX DA ORDEM {}", orderId);

        PagBankOrderResponseDto response = pagBankService.createPaymentPix(orderId);

        return ResponseEntity.ok(response);
    }

    // =========================================================================
    // 2. WEBHOOK DO PAGBANK
    // =========================================================================
    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveWebhook(
            @RequestBody PagBankWebhookDto webhook) {

        log.info("[CONTROLLER] WEBHOOK RECEBIDO DO PAGBANK → type={} eventId={}",
                webhook.getType(), webhook.getId());

        pagBankService.processWebhook(webhook);

        // RETORNAR 200 OK SEM CORPO É O PADRÃO DO PAGBANK
        return ResponseEntity.ok().build();
    }
}
