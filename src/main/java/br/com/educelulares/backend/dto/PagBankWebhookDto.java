package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

// -----------------------------------------------------------------------------
// DTO PARA RECEBER NOTIFICAÇÕES DO PAGBANK
// ESTE OBJETO REPRESENTA O PAYLOAD ENVIADO PELO WEBHOOK DO PAGBANK
// -----------------------------------------------------------------------------
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankWebhookDto {
    @JsonProperty("id")
    private String id;         // ID DA ORDEM NO PAGBANK
    @JsonProperty("reference_id")
    private String referenceId; // SEU ID INTERNO DO PEDIDO
    @JsonProperty("type")
    private String type;       // TIPO DO EVENTO (ex: "order.paid", "order.expired")
    @JsonProperty("status")
    private String status;     // STATUS ATUAL DO PAGAMENTO
}