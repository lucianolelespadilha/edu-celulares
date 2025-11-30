package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    // CÓDIGO DO PAÍS (EX: "55")
    // -------------------------------------------------------------------------
    @NotBlank(message = "Country code is required")
    @Pattern(
            regexp = "\\d{1,3}",
            message = "Country code must contain 1 to 3 digits"
    )
    private String country;

    // -------------------------------------------------------------------------
    // CÓDIGO DE ÁREA (DDD) DO CLIENTE (EX: "11", "21")
    // -------------------------------------------------------------------------
    @NotBlank(message = "Area code (DDD) is required")
    @Pattern(
            regexp = "\\d{2}",
            message = "Area code must contain exactly 2 digits"
    )
    private String area;

    // -------------------------------------------------------------------------
    // TELEFONE SEM FORMATAÇÃO
    // ACEITA DE 8 A 11 DÍGITOS (REGRAS DA ANATEL)
    // -------------------------------------------------------------------------
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "\\d{8,11}",
            message = "Phone number must contain 8 to 11 digits (numbers only)"
    )
    private String number;
}
