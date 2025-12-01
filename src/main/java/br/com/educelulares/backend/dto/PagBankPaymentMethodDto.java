package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONTÉM O QR CODE DO PIX RETORNADO PELO PAGBANK.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankPaymentMethodDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("qr_code")
    @Valid
    private PagBankQrCodeDto qrCode;
}
