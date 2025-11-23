package br.com.educelulares.backend.dto;

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
    private String href;

    // RELAÇÃO DO LINK (EX: "PAYMENT", "SELF", ETC.)
    private String rel;

    // FORMATO DA MÍDIA (EX: "APPLICATION/JSON")
    private String media;

    // TIPO DO LINK (EX: "GET", "POST")
    private String type;
}
