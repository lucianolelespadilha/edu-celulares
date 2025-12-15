package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO RESPONSÁVEL POR REPRESENTAR O QR CODE RETORNADO PELO PAGBANK.
 *
 * CAMPOS IMPORTANTES:
 * - text: A CHAVE PIX EM FORMATO TEXTO (COPIA E COLA)
 * - links: CONTÉM A URL DA IMAGEM PNG DO QR CODE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankQrCodeResponseDto {

    /** ID DO QR CODE */
    @JsonProperty("id")
    private String id;

    /** OBJETO COM O VALOR DO PIX (O MESMO MODELO DO REQUEST) */
    @Valid
    @JsonProperty("amount")
    private PagBankChargeAmountRequestDto amount;

    /** DATA/HORA DE EXPIRAÇÃO */
    @JsonProperty("expiration_date")
    private String expirationDate;

    /** CÓDIGO PIX EM TEXTO (COPIA E COLA) */
    @JsonProperty("text")
    private String text;

    /** LISTA DE LINKS CONTENDO, POR EXEMPLO, A IMAGEM DO QR CODE */
    @JsonProperty("links")
    private List<PagBankLinkResponseDto> links;
}
