package br.com.educelulares.backend.dto;

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
    private String name;

    // -------------------------------------------------------------------------
    // QUANTIDADE DO ITEM
    // EXEMPLO: 3 → o cliente comprou 3 unidades
    // -------------------------------------------------------------------------
    private Integer quantity;

    // -------------------------------------------------------------------------
    // PREÇO UNITÁRIO EM CENTAVOS
    // EXEMPLO: R$ 89,90 → enviar "8990"
    // -------------------------------------------------------------------------
    private Integer unit_amount;
}

