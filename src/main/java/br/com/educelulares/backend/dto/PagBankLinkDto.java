package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * REPRESENTA OS LINKS DE AÇÃO (SELF, PAY)
 * RETORNADOS PELO PAGBANK.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankLinkDto {

    @JsonProperty("rel")
    private String rel;

    @JsonProperty("href")
    private String href;

    @JsonProperty("media")
    private String media;

    @JsonProperty("type")
    private String type;
}
