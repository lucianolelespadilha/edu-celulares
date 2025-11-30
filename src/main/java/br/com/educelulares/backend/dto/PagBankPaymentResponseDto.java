package br.com.educelulares.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    // CÓDIGO DA RESPOSTA (SUCCESS, ERROR, INVALID_REQUEST, ETC.)
    @NotBlank(message = "Response code cannot be blank")
    private String code;

    // DESCRIÇÃO DO RESULTADO
    @NotBlank(message = "Response message cannot be blank")
    private String message;

    // QR CODE GERADO (PODE SER NULO EM ALGUNS TIPOS DE COBRANÇA)
    @Valid
    private PagBankQrCodeDto qr_code;

    // LINK DE PAGAMENTO (PODE SER NULO EM PIX)
    @Valid
    private PagBankLinkPaymentDto link;
}

