package br.com.educelulares.backend.controller.pagbank;

import br.com.educelulares.backend.client.PagBankClient;
import br.com.educelulares.backend.dto.pagbank.PagBankOrderResponseDto;
import br.com.educelulares.backend.service.pagbank.PagBankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/pagbank/orders")
@RequiredArgsConstructor
public class PagBankOrderQueryController {

    private final PagBankClient pagBankClient;
    private final PagBankService pagBankService;

    /**
     * CONSULTA DIRETA NO PAGBANK PELO ID DO PAGBANK
     * GET /api/pagbank/orders/{pagbankOrderId}
     */
    @GetMapping("/{pagbankOrderId}")
    public ResponseEntity<PagBankOrderResponseDto> getOrderStatus(
            @PathVariable String pagbankOrderId
    ) {
        log.info("[PAGBANK] CONSULTANDO STATUS DO PEDIDO {}", pagbankOrderId);

        PagBankOrderResponseDto response = pagBankClient.getOrderStatus(pagbankOrderId);

        return ResponseEntity.ok(response);
    }

    /**
     * SINCRONIZA O STATUS LOCAL DA ORDEM USANDO O pagbankOrderId SALVO NO BANCO
     * PUT /api/pagbank/orders/sync/{orderId}
     */
    @PutMapping("/sync/{orderId}")
    public ResponseEntity<String> syncOrderStatusPUT(
            @PathVariable Long orderId
    ) {
        log.info("[PAGBANK] SINCRONIZANDO (PUT) STATUS DO PEDIDO LOCAL {}", orderId);

        String updatedStatus = pagBankService.syncPaymentStatus(orderId);

        return ResponseEntity.ok("STATUS DO PEDIDO " + orderId + " ATUALIZADO PARA: " + updatedStatus);
    }

    /**
     * (Opcional) A mesma rota via GET — já existia antes
     * GET /api/pagbank/orders/{orderId}/sync
     */
    @GetMapping("/{orderId}/sync")
    public ResponseEntity<String> syncOrderStatusGET(
            @PathVariable Long orderId
    ) {
        log.info("[PAGBANK] SINCRONIZANDO (GET) STATUS DO PEDIDO LOCAL {}", orderId);

        String updatedStatus = pagBankService.syncPaymentStatus(orderId);

        return ResponseEntity.ok("STATUS DO PEDIDO " + orderId + " ATUALIZADO PARA: " + updatedStatus);
    }
}
