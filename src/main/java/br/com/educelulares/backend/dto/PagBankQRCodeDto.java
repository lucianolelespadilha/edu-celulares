package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    // IMAGEM DO QR CODE EM BASE64
    @NotBlank(message = "QR Code base64 is required")
    @Pattern(
            regexp = "^[A-Za-z0-9+/=]+$",
            message = "Invalid base64 format"
    )
    private String base64;

    // CONTEÚDO DO QR CODE (TEXTO CODIFICADO)
    @NotBlank(message = "QR Code content is required")
    private String content;
}
