package br.com.educelulares.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO RESPONSÁVEL POR REPRESENTAR A RESPOSTA DE PAGAMENTO RETORNADA PELO PAGBANK
// CONTÉM INFORMAÇÕES COMO CÓDIGO, MENSAGEM, QR CODE E LINK DE PAGAMENTO
// ESTE DTO É UTILIZADO PARA MAPEAR O RETORNO DAS REQUISIÇÕES DE CRIAÇÃO DE PAGAMENTO
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankPaymentResponseDto {

    // CÓDIGO RETORNADO PELO PAGBANK (EX: STATUS OU CÓDIGO DE ERRO)
    private String code;

    // MENSAGEM ASSOCIADA AO CÓDIGO (EX: "SUCCESS", "INVALID REQUEST")
    private String message;

    // OBJETO CONTENDO OS DADOS DO QR CODE GERADO PARA O PAGAMENTO
    private PagBankQRCodeDto qr_code;

    // LINK DE PAGAMENTO RETORNADO PELO PAGBANK
    private PagBankLinkPaymentDto link;
}
