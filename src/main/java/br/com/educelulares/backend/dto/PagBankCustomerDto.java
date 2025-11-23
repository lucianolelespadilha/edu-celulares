package br.com.educelulares.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO UTILIZADO PARA REPRESENTAR O CLIENTE EM UMA ORDEM ENVIADA AO PAGBANK.
// ESTE OBJETO CONTÉM INFORMAÇÕES BÁSICAS DO COMPRADOR COMO NOME, EMAIL E TELEFONE.
// É O FORMATO EXIGIDO PELA API DO PAGBANK PARA IDENTIFICAÇÃO DO PAGADOR.
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankCustomerDto {

    // -------------------------------------------------------------------------
    // NOME COMPLETO DO CLIENTE
    // UTILIZADO PARA IDENTIFICAÇÃO DO COMPRADOR NO PAGBANK
    // -------------------------------------------------------------------------
    private String name;

    // -------------------------------------------------------------------------
    // EMAIL DO CLIENTE
    // PAGBANK UTILIZA ESTE CAMPO PARA ENVIO DE CONFIRMAÇÕES E VALIDAÇÕES
    // -------------------------------------------------------------------------
    private String email;

    // -------------------------------------------------------------------------
    // TELEFONE DO CLIENTE
    // ENVOLVIDO NO PROCESSO DE IDENTIFICAÇÃO E CONFIRMAÇÃO DO PAGBANK
    // (DTO ESPECÍFICO: PagBankPhoneDto)
    // -------------------------------------------------------------------------
    private PagBankPhoneDto phone;
}

