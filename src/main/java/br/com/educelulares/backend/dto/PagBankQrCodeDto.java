package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONTÉM O QR CODE DO PIX (content + base64).
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankQrCodeDto {

    @JsonProperty("content")
    private String content;

    @JsonProperty("base64")
    private String base64;
}
