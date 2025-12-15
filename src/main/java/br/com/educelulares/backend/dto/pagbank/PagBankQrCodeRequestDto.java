package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO RESPONSÁVEL POR REPRESENTAR O OBJETO QR CODE NA REQUISIÇÃO DE CRIAÇÃO DE PEDIDO.
 *
 * ESTRUTURA ESPERADA PELO PAGBANK (EXEMPLO):
 *
 * "qr_codes": [
 *   {
 *     "amount": { "value": 500 },
 *     "expiration_date": "2025-12-31T23:59:59-03:00"
 *   }
 * ]
 *
 * CAMPOS:
 * - amount: OBJETO OBRIGATÓRIO QUE DEFINE O VALOR (EM CENTAVOS)
 * - expiration_date: OPCIONAL, STRING ISO-8601 COM OFFSET; SE AUSENTE, O PAGBANK USA 24 HORAS POR PADRÃO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankQrCodeRequestDto {

    /** OBJETO QUE REPRESENTA O VALOR DO QR CODE (EM CENTAVOS) */
    @NotNull(message = "O OBJETO amount DO QR CODE É OBRIGATÓRIO")
    @Valid
    @JsonProperty("amount")
    private PagBankChargeAmountRequestDto amount;

    /**
     * DATA/HORA DE EXPIRAÇÃO DO QR CODE EM FORMATO ISO-8601 COM OFFSET.
     *
     * EXEMPLO: "2025-12-31T23:59:59-03:00"
     *
     * ESTE CAMPO É OPCIONAL; SE NÃO INFORMADO, O PAGBANK USA A VALIDADE PADRÃO (24 HORAS).
     *
     * A VALIDAÇÃO ABAIXO É SIMPLES E VERIFICA UM FORMATO ISO-8601 BÁSICO COM OFFSET.
     * SE VOCÊ PREFERE ACEITAR QUALQUER STRING, REMOVA A ANOTAÇÃO @Pattern.
     */
    @Pattern(regexp = "^$|^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:[+-]\\d{2}:?\\d{2})$",
            message = "expiration_date DEVE SER EM FORMATO ISO-8601 COM OFFSET (OU VAZIO)")
    @JsonProperty("expiration_date")
    private String expirationDate;
}
