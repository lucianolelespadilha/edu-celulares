package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO QUE DEFINE O MÉTODO DE PAGAMENTO.
 *
 * PARA PIX, O PAGBANK EXIGE:
 *   "payment_method": { "type": "PIX" }
 *
 * OUTROS TIPOS EXISTEM, MAS ESTE MÓDULO USA APENAS PIX.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankPaymentMethodRequestDto {

    /** TIPO DO PAGAMENTO (SEMPRE "PIX" NESTE MÓDULO) */
    @NotBlank(message = "O TIPO DE PAGAMENTO É OBRIGATÓRIO (USE 'PIX')")
    @JsonProperty("type")
    private String type;
}
