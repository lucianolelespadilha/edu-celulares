package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank
    private String name;

    // -------------------------------------------------------------------------
    // EMAIL DO CLIENTE
    // PAGBANK UTILIZA ESTE CAMPO PARA ENVIO DE CONFIRMAÇÕES E VALIDAÇÕES
    // -------------------------------------------------------------------------
    @NotBlank
    private String email;

    @JsonProperty("tax_id")
    @NotBlank(message = "O campo tax_id é obrigatório para o PagBank.")
    @Pattern(
            regexp = "\\d{11}|\\d{14}",
            message = "O tax_id deve conter 11 (CPF) ou 14 dígitos (CNPJ), apenas números."
    )
    private String cpf;


    // -------------------------------------------------------------------------
    // TELEFONE DO CLIENTE
    // ENVOLVIDO NO PROCESSO DE IDENTIFICAÇÃO E CONFIRMAÇÃO DO PAGBANK
    // (DTO ESPECÍFICO: PagBankPhoneDto)
    // -------------------------------------------------------------------------
    @NotNull
    @Valid
    private PagBankPhoneDto phone;
}

