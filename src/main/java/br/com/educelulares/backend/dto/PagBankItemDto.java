package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO RESPONSÁVEL POR REPRESENTAR CADA ITEM DO PEDIDO ENVIADO AO PAGBANK.
// CADA ITEM CONTÉM NOME, QUANTIDADE E O VALOR UNITÁRIO (EM CENTAVOS).
// ESTE OBJETO COMPÕE A LISTA "items" DENTRO DO ORDER REQUEST DO PAGBANK.
// -----------------------------------------------------------------------------
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankItemDto {

    // -------------------------------------------------------------------------
    // NOME DO ITEM / PRODUTO QUE SERÁ MOSTRADO NO CHECKOUT DO PAGBANK
    // -------------------------------------------------------------------------
    @NotBlank
    private String name;

    // -------------------------------------------------------------------------
    // QUANTIDADE DO ITEM
    // EXEMPLO: 3 → o cliente comprou 3 unidades
    // -------------------------------------------------------------------------
    @NotNull
    private Integer quantity;

    // -------------------------------------------------------------------------
    // PREÇO UNITÁRIO EM CENTAVOS
    // EXEMPLO: R$ 89,90 → enviar "8990"
    // -------------------------------------------------------------------------
    @NotNull
    private Integer unit_amount;
}

