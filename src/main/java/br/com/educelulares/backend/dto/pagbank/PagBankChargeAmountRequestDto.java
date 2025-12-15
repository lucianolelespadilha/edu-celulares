package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO RESPONSÁVEL POR REPRESENTAR O VALOR DA COBRANÇA (EM CENTAVOS).
 *
 * ESTE OBJETO É UTILIZADO DENTRO DO BLOCO:
 *   charges[].amount.value
 *
 * EXEMPLO:
 *   "amount": { "value": 500 }
 *
 * ONDE 500 = R$ 5,00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankChargeAmountRequestDto {

    /** VALOR DO PIX EM CENTAVOS */
    @NotNull(message = "O VALOR DO PIX (amount.value) É OBRIGATÓRIO")
    @Min(value = 1, message = "O VALOR DO PIX DEVE SER MAIOR QUE ZERO")
    @JsonProperty("value")
    private Integer value;
}
