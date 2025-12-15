package br.com.educelulares.backend.dto.pagbank;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO PRINCIPAL DO WEBHOOK ENVIADO PELO PAGBANK.
 *
 * FORMATO REAL:
 * {
 *   "id": "EVENT-123456",
 *   "type": "ORDER.STATUS",
 *   "data": {
 *        "id": "ORD-123456",
 *        "reference_id": "10",
 *        "status": "PAID"
 *   }
 * }
 *
 * ESTE OBJETO É RECEBIDO PELA CONTROLLER E ENVIADO AO SERVICE.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankWebhookDto {

    /** ID DO EVENTO DE WEBHOOK (NÃO CONFUNDIR COM ID DO PEDIDO) */
    @JsonProperty("id")
    private String id;

    /** TIPO DO EVENTO (EX: "ORDER.STATUS") */
    @JsonProperty("type")
    private String type;

    /** BOLCO DATA COM ID, REFERENCE_ID E STATUS */
    @JsonProperty("data")
    private PagBankWebhookDataDto data;

    // ==================================================================
    // MÉTODOS DE ACESSO FACILITADOS, USADOS NO PagBankService
    // ==================================================================

    /** RETORNA O ID DO PEDIDO NO PAGBANK */
    public String getPaymentId() {
        return data != null ? data.getId() : null;
    }

    /** RETORNA O REFERENCE_ID QUE USAMOS PARA BUSCAR A ORDEM LOCAL */
    public String getReferenceId() {
        return data != null ? data.getReferenceId() : null;
    }

    /** RETORNA O STATUS ATUAL DO PAGBANK */
    public String getStatus() {
        return data != null ? data.getStatus() : null;
    }
}


