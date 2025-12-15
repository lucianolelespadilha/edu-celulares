package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO RESPONSÁVEL POR REPRESENTAR UM ITEM DA ORDEM ENVIADA AO PAGBANK.
 *
 * CAMPOS OBRIGATÓRIOS:
 *  - NAME: NOME DO PRODUTO OU SERVIÇO
 *  - QUANTITY: QUANTIDADE DO ITEM
 *  - UNIT_AMOUNT: VALOR UNITÁRIO EM CENTAVOS (EX: 500 = R$ 5,00)
 *
 * ESTE DTO É UTILIZADO DENTRO DA LISTA "items" DO PagBankOrderRequestDto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankItemDto {

    /** NOME DO PRODUTO */
    @NotBlank(message = "O NOME DO ITEM É OBRIGATÓRIO")
    @JsonProperty("name")
    private String name;

    /** QUANTIDADE DO ITEM (PRECISA SER >= 1) */
    @NotNull(message = "A QUANTIDADE DO ITEM É OBRIGATÓRIA")
    @Min(value = 1, message = "A QUANTIDADE DEVE SER NO MÍNIMO 1")
    @JsonProperty("quantity")
    private Integer quantity;

    /**
     * VALOR UNITÁRIO DO ITEM EM CENTAVOS.
     *
     * EXEMPLO:
     *  R$ 10,00 = 1000
     *  R$ 5,50  = 550
     */
    @NotNull(message = "O VALOR UNITÁRIO (unit_amount) É OBRIGATÓRIO")
    @Min(value = 1, message = "O VALOR UNITÁRIO PRECISA SER MAIOR QUE ZERO")
    @JsonProperty("unit_amount")
    private Integer unitAmount;
}
