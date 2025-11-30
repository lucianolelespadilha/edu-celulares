package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/// -----------------------------------------------------------------------------
// DTO PARA RECEBER NOTIFICAÇÕES DO PAGBANK
// ESTE OBJETO REPRESENTA O PAYLOAD ENVIADO PELO WEBHOOK DO PAGBANK
// -----------------------------------------------------------------------------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagBankWebhookDto {

    @NotBlank
    @JsonProperty("id")
    private String id;  // ID GERADO PELO PAGBANK

    @NotBlank
    @JsonProperty("reference_id")
    private String referenceId; // ID DO PEDIDO NO SEU SISTEMA

    @NotBlank
    @JsonProperty("type")
    private String type; // order.paid, order.expired, order.failed

    @NotBlank
    @JsonProperty("status")
    private String status; // PAID, WAITING_PAYMENT, EXPIRED, FAILED
}
