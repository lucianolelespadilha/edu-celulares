package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO RESPONSÁVEL POR REPRESENTAR OS LINKS DE PAGAMENTO RETORNADOS PELO PAGBANK
// CONTÉM INFORMAÇÕES COMO HREF, TIPO DE LINK, MÍDIA E RELAÇÃO
// ESTE DTO É UTILIZADO PARA MAPEAR OS CAMPOS DO OBJETO "LINKS" DA API EXTERNA
// -----------------------------------------------------------------------------

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankLinkPaymentDto {

    // URL DO RECURSO RETORNADO PELO PAGBANK (EX: LINK DE PAGAMENTO)
    @NotBlank(message = "href (URL) is required")
    @Pattern(regexp = "https?://.*", message = "href must be a valid URL starting with http or https")
    private String href;

    // RELAÇÃO DO LINK (EX: PAYMENT, SELF, QRCODE)
    @NotBlank(message = "rel is required")
    private String rel;

    // MEDIA TYPE (EX: APPLICATION/JSON)
    @NotBlank(message = "media type is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._+-]+/[a-zA-Z0-9._+-]+$",
            message = "media must be a valid MIME type (e.g., application/json)"
    )
    private String media;

    // MÉTODO HTTP (GET, POST, PUT, DELETE)
    @NotBlank(message = "type is required")
    @Pattern(
            regexp = "GET|POST|PUT|DELETE",
            message = "type must be GET, POST, PUT, or DELETE"
    )
    private String type;
}


