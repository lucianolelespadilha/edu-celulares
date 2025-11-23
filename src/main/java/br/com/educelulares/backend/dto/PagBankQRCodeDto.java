package br.com.educelulares.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO UTILIZADO PARA RECEBER OS DADOS DE QR CODE RETORNADOS PELO PAGBANK.
// NORMALMENTE ESTE OBJETO É RETORNADO QUANDO O CLIENTE ESCOLHE PAGAMENTO VIA QR.
// ELE PODE CONTER O QR EM BASE64 E A STRING "CONTENT" PARA EXIBIÇÃO NO FRONT-END.
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankQRCodeDto {

    // -------------------------------------------------------------------------
    // IMAGEM DO QR CODE EM FORMATO BASE64
    // PERMITE GERAR UMA IMAGEM DIRETO NO FRONT-END
    // -------------------------------------------------------------------------
    private String base64;

    // -------------------------------------------------------------------------
    // CONTEÚDO DO QR CODE (GERALMENTE TEXTO CODIFICADO)
    // USADO PARA APRESENTAR OU GERAR MANUALMENTE O QR CODE SE NECESSÁRIO
    // -------------------------------------------------------------------------
    private String content;
}
