package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO RESPONSÁVEL POR REPRESENTAR O VALOR DO FRETE (shipping.amount)
 * EXIGIDO NA ESTRUTURA DE PEDIDOS DO PAGBANK.
 *
 * ESTRUTURA ESPERADA:
 *
 * "shipping": {
 *   "address": {...},
 *   "amount": {
 *     "value": 500
 *   }
 * }
 *
 * OBS:
 *  - O VALOR DEVE SER INFORMADO EM CENTAVOS.
 *  - A VARIÁVEL value É OBRIGATÓRIA.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankShippingAmountDto {

    /** VALOR DO FRETE EM CENTAVOS (EX: 500 = R$ 5,00) */
    @NotNull(message = "O CAMPO amount.value É OBRIGATÓRIO NO SHIPPING")
    @JsonProperty("value")
    private Integer value;
}
