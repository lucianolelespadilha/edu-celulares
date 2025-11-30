package br.com.educelulares.backend.dto;

import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.boot.autoconfigure.web.WebProperties;

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
    @NotBlank(message = "Link ID is required")
    private String id;

    // STATUS DO LINK (ACTIVE, PAID, EXPIRED, CANCELED)
    @NotBlank(message = "Link status is required")
    @Pattern(
            regexp = "ACTIVE|PAID|EXPIRED|CANCELED",
            message = "Invalid link status. Allowed: ACTIVE, PAID, EXPIRED, CANCELED"
    )
    private String status;

    // DADOS DETALHADOS DA COBRANÇA (OPCIONAL)
    @Valid
    private PagBankPaymentResponseDto payment_response;
}

