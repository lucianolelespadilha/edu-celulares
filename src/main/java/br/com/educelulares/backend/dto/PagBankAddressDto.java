package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

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
    @NotBlank(message = "Street is required")
    private String street;

    // -------------------------------------------------------------------------
    // NÚMERO DO ENDEREÇO (RESIDÊNCIA, APTO, PRÉDIO ETC.)
    // Pode conter letras (ex: 12A, 80-B)
    // -------------------------------------------------------------------------
    @NotBlank(message = "Number is required")
    private String number;

    // -------------------------------------------------------------------------
    // COMPLEMENTO (OPCIONAL)
    // -------------------------------------------------------------------------
    private String complement;

    // -------------------------------------------------------------------------
    // BAIRRO DO CLIENTE
    // -------------------------------------------------------------------------
    @NotBlank(message = "Locality (district) is required")
    private String locality;

    // -------------------------------------------------------------------------
    // CIDADE DO CLIENTE
    // -------------------------------------------------------------------------
    @NotBlank(message = "City is required")
    private String city;

    // -------------------------------------------------------------------------
    // REGIÃO / NOME DO ESTADO (EX: 'São Paulo')
    // -------------------------------------------------------------------------
    @NotBlank(message = "Region is required")
    private String region;

    // -------------------------------------------------------------------------
    // UF DO ESTADO — 2 LETRAS (EX: 'SP')
    // -------------------------------------------------------------------------
    @NotBlank(message = "Region code (UF) is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Region code must be 2 uppercase letters (e.g., SP)")
    private String region_code;

    // -------------------------------------------------------------------------
    // PAÍS (EX: 'BRA')
    // -------------------------------------------------------------------------
    @NotBlank(message = "Country is required")
    @Pattern(regexp = "^[A-Z]{2,3}$", message = "Country must be 2–3 uppercase letters (e.g., BRA)")
    private String country;

    // -------------------------------------------------------------------------
    // CEP: APENAS NÚMEROS, 8 DÍGITOS (SEM HÍFEN)
    // EX: 01001000
    // -------------------------------------------------------------------------
    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^[0-9]{8}$", message = "Postal code must contain exactly 8 digits (numbers only)")
    private String postal_code;
}
