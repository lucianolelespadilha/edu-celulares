package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO RESPONSÁVEL PELO CLIENTE (COMPRADOR) ENVIADO AO PAGBANK.
 *
 * CAMPOS OBRIGATÓRIOS:
 *  - NAME: NOME COMPLETO DO CLIENTE
 *  - EMAIL: EMAIL DO CLIENTE
 *  - TAX_ID: CPF OU CNPJ
 *  - PHONES: LISTA DE TELEFONES
 *
 * ATENÇÃO:
 * O PAGBANK PERMITE MULTIPLOS TELEFONES, ENTÃO ESTE DTO SEGUE A DOCUMENTAÇÃO OFICIAL.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankCustomerDto {

    /** NOME COMPLETO DO CLIENTE */
    @NotBlank(message = "O NOME DO CLIENTE É OBRIGATÓRIO")
    @JsonProperty("name")
    private String name;

    /** EMAIL DO CLIENTE (VALIDAÇÃO DE FORMATO) */
    @NotBlank(message = "O EMAIL DO CLIENTE É OBRIGATÓRIO")
    @Email(message = "FORMATO DE EMAIL INVÁLIDO")
    @JsonProperty("email")
    private String email;

    /** CPF OU CNPJ DO CLIENTE (APENAS NÚMEROS) */
    @NotBlank(message = "O CPF/CNPJ DO CLIENTE É OBRIGATÓRIO")
    @JsonProperty("tax_id")
    private String taxId;

    /** LISTA DE TELEFONES DO CLIENTE */
    @NotEmpty(message = "É OBRIGATÓRIO INFORMAR AO MENOS UM TELEFONE")
    @Valid
    @JsonProperty("phones")
    private List<PagBankPhoneDto> phones;
}
