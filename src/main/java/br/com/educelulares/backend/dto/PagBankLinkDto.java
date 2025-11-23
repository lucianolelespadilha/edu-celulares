package br.com.educelulares.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO RESPONSÁVEL POR REPRESENTAR OS LINKS DE PAGAMENTO GERADOS PELO PAGBANK
// ESTE OBJETO É RETORNADO EM ALGUMAS OPERAÇÕES DE PAGAMENTO, INCLUINDO STATUS
// E A RESPOSTA COMPLETA DE PAGAMENTO (QR CODE, LINK, CÓDIGO, ETC.)
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankLinkDto {

    // IDENTIFICADOR ÚNICO DO LINK GERADO PELO PAGBANK
    private String id;

    // STATUS ATUAL DO LINK (EX: "ACTIVE", "PAID", "EXPIRED")
    private String status;

    // RESPOSTA COMPLETA DE PAGAMENTO ASSOCIADA A ESTE LINK
    private PagBankPaymentResponseDto payment_response;
}
