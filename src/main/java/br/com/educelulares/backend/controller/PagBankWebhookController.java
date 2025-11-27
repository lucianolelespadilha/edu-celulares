package br.com.educelulares.backend.controller;




import br.com.educelulares.backend.dto.PagBankWebhookDto;
import br.com.educelulares.backend.service.PagBankService;
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
@RequestMapping("/pagbank/webhook")
@RequiredArgsConstructor
public class PagBankWebhookController {

    private final PagBankService pagBankService;

    // -------------------------------------------------------------------------
    // ENDPOINT QUE RECEBERÁ AS NOTIFICAÇÕES DO PAGBANK
    // ESTE MÉTODO PRECISA RETORNAR 200 OK SEMPRE QUE RECEBER UMA NOTIFICAÇÃO
    // SE VOCÊ RETORNAR OUTRO CÓDIGO, O PAGBANK TENTA ENVIAR DE NOVO
    // -------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<Void> receiveWebhook(@RequestBody PagBankWebhookDto payload) {

        log.info("WEBHOOK RECEBIDO DO PAGBANK: {}", payload);

        // CHAMA O SERVIÇO PARA PROCESSAR O WEBHOOK
        pagBankService.processWebhook(payload);

        return ResponseEntity.ok().build();
    }
}
