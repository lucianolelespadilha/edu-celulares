package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * REPRESENTA O BLOCO "data" ENVIADO EM UMA NOTIFICAÇÃO WEBHOOK DO PAGBANK.
 *
 * EXEMPLO:
 *  "data": {
 *      "id": "ORD-123456",
 *      "reference_id": "10",
 *      "status": "PAID"
 *  }
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankWebhookDataDto {

    /** ID DO PEDIDO NO PAGBANK (EX: ORD-123456789) */
    @JsonProperty("id")
    private String id;

    /** REFERENCE_ID QUE ENVIAMOS NO REQUEST */
    @JsonProperty("reference_id")
    private String referenceId;

    /** STATUS ATUAL DO PAGAMENTO (PAID, WAITING_PAYMENT, EXPIRED...) */
    @JsonProperty("status")
    private String status;
}
