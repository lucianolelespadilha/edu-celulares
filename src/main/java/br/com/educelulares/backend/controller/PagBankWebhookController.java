package br.com.educelulares.backend.controller;




import br.com.educelulares.backend.dto.PagBankWebhookDto;
import br.com.educelulares.backend.service.PagBankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// -----------------------------------------------------------------------------
// CONTROLLER RESPONSÁVEL POR RECEBER AS NOTIFICAÇÕES (WEBHOOKS) DO PAGBANK
// ESTA ROTA PRECISA SER PÚBLICA E SEM AUTENTICAÇÃO
// -----------------------------------------------------------------------------
@Slf4j
@RestController
@RequestMapping("/webhook/pagbank")
@RequiredArgsConstructor
public class PagBankWebhookController {

    private final PagBankService pagBankService;

    // -------------------------------------------------------------------------
    // RECEBE NOTIFICAÇÕES DO PAGBANK
    // SEMPRE RETORNA 200 OK PARA EVITAR REENVIO DESNECESSÁRIO
    // -------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<Void> receiveWebhook(
            @Valid @RequestBody PagBankWebhookDto payload) {

        log.info("WEBHOOK PAGBANK RECEBIDO: id={}, reference_id={}, type={}, status={}",
                payload.getId(),
                payload.getReferenceId(),
                payload.getType(),
                payload.getStatus()
        );

        try {
            pagBankService.processWebhook(payload);
        } catch (Exception e) {
            log.error("ERRO AO PROCESSAR WEBHOOK PAGBANK: {}", e.getMessage());
            // Importante: SEMPRE retornamos 200 para o PagBank
        }

        return ResponseEntity.ok().build();
    }
}
