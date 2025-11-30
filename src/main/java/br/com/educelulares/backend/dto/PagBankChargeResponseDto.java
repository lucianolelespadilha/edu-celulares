package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * REPRESENTA UMA COBRANÇA (CHARGE) RETORNADA PELO PAGBANK.
 *
 * CADA ORDEM PODE CONTER UMA OU MAIS "CHARGES".
 * EM PAGAMENTOS PIX, É AQUI QUE VEM:
 *  - O TIPO DE PAGAMENTO (PIX)
 *  - O QR CODE
 *  - A IMAGEM BASE64 DO QR CODE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankChargeResponseDto {

    // -------------------------------------------------------------------------
    // MÉTODO DE PAGAMENTO DA CHARGE (PIX)
    // CONTÉM O QR CODE E O TYPE
    // -------------------------------------------------------------------------
    @JsonProperty("payment_method")
    @Valid
    private PagBankPaymentMethodDto paymentMethod;
}
