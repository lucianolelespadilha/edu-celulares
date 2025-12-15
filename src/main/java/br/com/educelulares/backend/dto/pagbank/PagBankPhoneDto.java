package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
        * DTO RESPONSÁVEL PELOS DADOS DO TELEFONE DO CLIENTE.
        *
        * FORMATO OBRIGATÓRIO DO PAGBANK:
        *  {
        *      "country": "55",
        *      "area": "11",
        *      "number": "999999999",
        *      "type": "MOBILE"
        *  }
        */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankPhoneDto {
    /** CÓDIGO DO PAÍS (EX: 55 PARA BRASIL) */
    @NotBlank(message = "O CÓDIGO DO PAÍS É OBRIGATÓRIO")
    @JsonProperty("country")
    private String country;
    /** DDD (EX: 11) */
    @NotBlank(message = "O DDD DO TELEFONE É OBRIGATÓRIO")
    @JsonProperty("area")
    private String area;
    /** NÚMERO DO TELEFONE (SEM TRAÇOS) */
    @NotBlank(message = "O NÚMERO DO TELEFONE É OBRIGATÓRIO")
    @JsonProperty("number")
    private String number;
    /** TIPO DO TELEFONE — SEMPRE MOBILE PARA PIX */
    @JsonProperty("type")
    private String type = "MOBILE";
}
