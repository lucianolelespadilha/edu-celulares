package br.com.educelulares.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO UTILIZADO PARA REPRESENTAR O TELEFONE DO CLIENTE NO PAGBANK.
// ESTE OBJETO FAZ PARTE DA ESTRUTURA DO PagBankCustomerDto E SEGUE O PADRÃO
// EXIGIDO PELA API DO PAGBANK PARA DADOS DE CONTATO DO COMPRADOR.
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankPhoneDto {

    // -------------------------------------------------------------------------
    // CÓDIGO DO PAÍS (EXEMPLO: "55" PARA BRASIL)
    // UTILIZADO PARA FORMATAÇÃO E VALIDAÇÃO DO TELEFONE PELO PAGBANK
    // -------------------------------------------------------------------------
    private String country;

    // -------------------------------------------------------------------------
    // CÓDIGO DE ÁREA (DDD) DO CLIENTE (EXEMPLO: "11", "21", "48"...)
    // -----------------------------------------------------------------------------
    private String area;

    // -------------------------------------------------------------------------
    // NÚMERO DE TELEFONE DO CLIENTE (SEM DDD E SEM FORMATAÇÃO)
    // EXEMPLO: "998877665"
    // -----------------------------------------------------------------------------
    private String number;
}
