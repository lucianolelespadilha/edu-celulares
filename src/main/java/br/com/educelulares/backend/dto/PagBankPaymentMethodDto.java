package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * REPRESENTA O MÉTODO DE PAGAMENTO UTILIZADO NA CHARGE.
 *
 * NO PIX, ESTE OBJETO CONTÉM:
 *  - O TIPO DO PAGAMENTO ("PIX")
 *  - O OBJETO qr_code, QUE POSSUI:
 *      - content (payload copiável do PIX)
 *      - base64 (imagem PNG do QR Code em base64)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankPaymentMethodDto {

    // -------------------------------------------------------------------------
    // TIPO DO PAGAMENTO DEFINIDO PELO PAGBANK
    // PARA PIX SEMPRE VIRÁ "PIX"
    // -------------------------------------------------------------------------
    @JsonProperty("type")
    private String type;

    // -------------------------------------------------------------------------
    // QR CODE DO PAGAMENTO PIX
    // CONTÉM O PAYLOAD E A IMAGEM EM BASE64
    // -------------------------------------------------------------------------
    @JsonProperty("qr_code")
    @Valid
    private PagBankQrCodeDto qrCode;
}
