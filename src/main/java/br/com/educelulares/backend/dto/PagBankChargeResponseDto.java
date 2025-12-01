package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO DE RESPOSTA DAS "CHARGES".
 * AQUI VEM O QR CODE DO PIX.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankChargeResponseDto {

    @JsonProperty("payment_method")
    @Valid
    private PagBankPaymentMethodDto paymentMethod;
}
