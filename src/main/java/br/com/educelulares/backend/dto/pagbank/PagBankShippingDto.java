package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO PRINCIPAL DO BLOCO "shipping" DO PAGBANK.
 *
 * CONTÉM:
 *  - ADDRESS: ENDEREÇO COMPLETO DO CLIENTE
 *  - AMOUNT: OBJETO COM O VALOR DO FRETE (EM CENTAVOS)
 *
 * ESTRUTURA ESPERADA PELO PAGBANK:
 *  "shipping": {
 *      "address": { ... },
 *      "amount": { "value": 0 }
 *  }
 *
 * MESMO QUANDO NÃO HÁ ENTREGA FÍSICA, É COMUM INFORMAR 0 PARA MANTER PADRÃO.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankShippingDto {

    /** ENDEREÇO COMPLETO DO CLIENTE */
    @NotNull(message = "O ENDEREÇO (shipping.address) É OBRIGATÓRIO")
    @Valid
    @JsonProperty("address")
    private PagBankAddressDto address;

    /** VALOR DO FRETE (amount.value), EM CENTAVOS */
    @NotNull(message = "O VALOR DO FRETE (shipping.amount) É OBRIGATÓRIO")
    @Valid
    @JsonProperty("amount")
    private PagBankShippingAmountDto amount;
}
