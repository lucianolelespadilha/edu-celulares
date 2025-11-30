package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO UTILIZADO PARA RECEBER OS DADOS DE QR CODE RETORNADOS PELO PAGBANK.
// ELE REPRESENTA EXATAMENTE O OBJETO "qr_code" DO JSON:
// {
//     "qr_code": {
//         "content": "...",
//         "base64": "..."
//     }
// }
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankQrCodeDto {

    // -------------------------------------------------------------------------
    // PAYLOAD COMPLETO DO QR CODE PIX (STRING COPIÁVEL)
    // -------------------------------------------------------------------------
    @JsonProperty("content")
    private String content;

    // -------------------------------------------------------------------------
    // IMAGEM DO QR CODE EM BASE64 (PODE VIR NULA EM ALGUNS CENÁRIOS)
    // -------------------------------------------------------------------------
    @JsonProperty("base64")
    private String base64;
}
