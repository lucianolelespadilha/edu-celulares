package br.com.educelulares.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO RESPONSÁVEL POR REPRESENTAR O ENDEREÇO DO CLIENTE NO FORMATO EXIGIDO
// PELA API DO PAGBANK. ESTE OBJETO É ENVIADO JUNTO À CRIAÇÃO DO PEDIDO
// PARA IDENTIFICAR O LOCAL DE ENTREGA OU ENDEREÇO DE REFERÊNCIA.
// -----------------------------------------------------------------------------
// CADA CAMPO MAPEIA DIRETAMENTE UMA PROPRIEDADE DO JSON DE ENVIO
// OS NOMES SEGUEM O PADRÃO EXATO UTILIZADO PELO PAGBANK
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankAddressDto {

    // -------------------------------------------------------------------------
    // NOME DA RUA DO ENDEREÇO DO CLIENTE
    // -------------------------------------------------------------------------
    private String street;

    // -------------------------------------------------------------------------
    // NÚMERO DO ENDEREÇO (RESIDÊNCIA, PRÉDIO, CASA ETC.)
    // -------------------------------------------------------------------------
    private String number;

    // -------------------------------------------------------------------------
    // COMPLEMENTO (APARTAMENTO, BLOCO, CASA FUNDOS, ETC.)
    // -------------------------------------------------------------------------
    private String complement;

    // -------------------------------------------------------------------------
    // BAIRRO OU LOCALIDADE DO CLIENTE
    // -------------------------------------------------------------------------
    private String locality;

    // -------------------------------------------------------------------------
    // CIDADE DO ENDEREÇO DO CLIENTE
    // -------------------------------------------------------------------------
    private String city;

    // -------------------------------------------------------------------------
    // REGIÃO DO CLIENTE (NOME DO ESTADO)
    // EXEMPLO: "São Paulo"
    // -------------------------------------------------------------------------
    private String region;

    // -------------------------------------------------------------------------
    // CÓDIGO DA REGIÃO (SIGLA DO ESTADO)
    // EXEMPLO: "SP"
    // -------------------------------------------------------------------------
    private String region_code;

    // -------------------------------------------------------------------------
    // NOME DO PAÍS
    // GERALMENTE "BRA"
    // -------------------------------------------------------------------------
    private String country;

    // -------------------------------------------------------------------------
    // CÓDIGO POSTAL (CEP)
    // SOMENTE NÚMEROS, SEM TRAÇO
    // EXEMPLO: "01001000"
    // -------------------------------------------------------------------------
    private String postal_code;
}
