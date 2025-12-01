package br.com.educelulares.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO UTILIZADO NO REQUEST PARA INDICAR O MÉTODO DE PAGAMENTO PARA O PAGBANK.
 * NESTE CASO, SEMPRE USAREMOS O TIPO "PIX" PARA GERAR O QR CODE.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankPaymentMethodRequestDto {

    // -------------------------------------------------------------------------
    // TIPO DO PAGAMENTO (PARA PIX SEMPRE ENVIAMOS "PIX")
    // -------------------------------------------------------------------------
    private String type;
}
