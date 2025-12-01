package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO UTILIZADO NO REQUEST PARA SOLICITAR A GERAÇÃO DE UMA CHARGE AO PAGBANK.
 * UMA "CHARGE" É A COBRANÇA QUE CONTÉM O MÉTODO DE PAGAMENTO.
 *
 * PARA PAGAMENTO PIX, A CHARGE DEVE CONTER:
 *    payment_method.type = "PIX"
 *
 * SEM ESTE OBJETO, O PAGBANK NUNCA GERA O QR CODE NO RESPONSE.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankChargeRequestDto {

    // -------------------------------------------------------------------------
    // payment_method: CONTÉM O TIPO DE PAGAMENTO ("PIX")
    // ESTE CAMPO É OBRIGATÓRIO PARA O PAGBANK GERAR O PIX AUTOMATICAMENTE.
    // -------------------------------------------------------------------------
    @JsonProperty("payment_method")
    @Valid
    private PagBankPaymentMethodRequestDto paymentMethod;
}
