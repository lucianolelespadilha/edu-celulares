package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO PRINCIPAL QUE REPRESENTA UMA COBRANÇA DENTRO DO PEDIDO DO PAGBANK.
 *
 * ESTRUTURA OFICIAL:
 *   "charges": [
 *     {
 *       "amount": { "value": 500 },
 *       "payment_method": { "type": "PIX" }
 *     }
 *   ]
 *
 * PARA PIX, SEMPRE TEREMOS:
 *  - amount: OBJETO COM O VALOR
 *  - payment_method: TIPO PIX
 *
 * O PagBank permette múltiplas cobranças, porém este projeto
 * utiliza apenas UMA cobrança PIX por pedido.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankChargeRequestDto {

    /** OBJETO amount COM O VALOR DO PIX (EM CENTAVOS) */
    @NotNull(message = "O OBJETO amount É OBRIGATÓRIO")
    @Valid
    @JsonProperty("amount")
    private PagBankChargeAmountRequestDto amount;

    /** OBJETO payment_method CONTENDO TIPO PIX */
    @NotNull(message = "O OBJETO payment_method É OBRIGATÓRIO")
    @Valid
    @JsonProperty("payment_method")
    private PagBankPaymentMethodRequestDto paymentMethod;
}

