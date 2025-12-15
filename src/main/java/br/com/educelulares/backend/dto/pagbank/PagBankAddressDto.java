package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO RESPONSÁVEL PELO ENDEREÇO DO CLIENTE,
 * UTILIZADO NO BLOCO "shipping.address" DA API PAGBANK.
 *
 * CAMPOS OBRIGATÓRIOS:
 *  - STREET: NOME DA RUA
 *  - NUMBER: NÚMERO
 *  - LOCALITY: BAIRRO
 *  - CITY: CIDADE
 *  - REGION_CODE: ESTADO (EX: SP, RJ)
 *  - COUNTRY: SEMPRE "BRA" PARA ENDEREÇOS NO BRASIL
 *  - POSTAL_CODE: CEP APENAS NUMEROS
 *
 * COMPLEMENT É OPCIONAL.
 *
 * ESTE DTO SEGUE O FORMATO OFICIAL DA DOCUMENTAÇÃO PAGBANK.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankAddressDto {

    /**
     * RUA / AVENIDA / LOGRADOURO
     */
    @NotBlank(message = "O CAMPO street É OBRIGATÓRIO")
    @JsonProperty("street")
    private String street;

    /**
     * NÚMERO DO ENDEREÇO
     */
    @NotBlank(message = "O CAMPO number É OBRIGATÓRIO")
    @JsonProperty("number")
    private String number;

    /**
     * COMPLEMENTO (OPCIONAL)
     */
    @JsonProperty("complement")
    private String complement;

    /**
     * BAIRRO / LOCALIDADE
     */
    @NotBlank(message = "O CAMPO locality É OBRIGATÓRIO")
    @JsonProperty("locality")
    private String locality;

    /**
     * CIDADE
     */
    @NotBlank(message = "O CAMPO city É OBRIGATÓRIO")
    @JsonProperty("city")
    private String city;

    /**
     * ESTADO / UF (EX: SP, RJ)
     */
    @NotBlank(message = "O CAMPO region_code É OBRIGATÓRIO")
    @JsonProperty("region_code")
    private String regionCode;

    /**
     * CÓDIGO DO PAÍS — DEVE SER 'BRA' PARA BRASIL
     */
    @NotBlank(message = "O CAMPO country É OBRIGATÓRIO (USE 'BRA')")
    @JsonProperty("country")
    private String country;

    /**
     * CEP APENAS NÚMEROS
     */
    @NotBlank(message = "O CAMPO postal_code É OBRIGATÓRIO")
    @JsonProperty("postal_code")
    private String postalCode;
}
