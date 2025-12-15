package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO RESPONSÁVEL POR REPRESENTAR OS LINKS RETORNADOS PELO PAGBANK,
 * NORMALMENTE USADOS PARA OBTER A IMAGEM DO QR CODE.
 *
 * EXEMPLO:
 *  "links": [
 *      { "rel": "QRCODE.PNG", "href": "...", "media": "image/png" }
 *  ]
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankLinkResponseDto {

    /** TIPO DO LINK (EX: "QRCODE.PNG") */
    @JsonProperty("rel")
    private String rel;

    /** URL A SER ACESSADA */
    @JsonProperty("href")
    private String href;

    /** TIPO DO CONTEÚDO (EX: image/png, text/plain) */
    @JsonProperty("media")
    private String media;
}
