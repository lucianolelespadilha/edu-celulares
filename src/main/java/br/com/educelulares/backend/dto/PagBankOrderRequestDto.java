package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * ============================================================================
 * DTO PRINCIPAL ENVIADO PARA O PAGBANK AO CRIAR UMA ORDEM PIX.
 *
 * ESTE OBJETO REPRESENTA EXATAMENTE O JSON DE ENVIO.
 *
 * ESTRUTURA:
 * {
 *   "reference_id": "...",
 *   "customer": { ... },
 *   "items": [ ... ],
 *   "shipping": { ... },
 *   "notification_urls": [ ... ],
 *   "charges": [ ... ]
 * }
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankOrderRequestDto {

    // =========================================================================
    // ID DO PEDIDO NO SEU SISTEMA
    // ENVIADO PARA O PAGBANK COMO "reference_id"
    // =========================================================================
    @NotBlank(message = "reference_id is required")
    @JsonProperty("reference_id")
    private String referenceId;

    // =========================================================================
    // DADOS DO CLIENTE
    // CONTÉM NOME, EMAIL, TELEFONE E CPF
    // =========================================================================
    @NotNull(message = "customer is required")
    @Valid
    private PagBankCustomerDto customer;

    // =========================================================================
    // LISTA DOS ITENS DA ORDEM
    // NÃO PODE SER VAZIA
    // =========================================================================
    @NotNull(message = "items list cannot be null")
    @Size(min = 1, message = "order must contain at least 1 item")
    @Valid
    private List<PagBankItemDto> items;

    // =========================================================================
    // INFORMAÇÕES DE ENVIO (ENDEREÇO + VALOR DO FRETE)
    // =========================================================================
    @NotNull(message = "shipping info is required")
    @Valid
    private PagBankShippingDto shipping;

    // =========================================================================
    // LISTA COM AS URLs DE NOTIFICAÇÃO PARA O WEBHOOK
    // DEVE APARECER COMO "notification_urls" NO JSON FINAL
    // =========================================================================
    @NotNull(message = "notification_urls cannot be null")
    @Size(min = 1, message = "at least one notification URL must be provided")
    @JsonProperty("notification_urls")
    private List<
            @Pattern(
                    regexp = "https?://.*",
                    message = "each notification URL must be a valid http/https address"
            )
                    String> notificationUrls;

    // =========================================================================
    // LISTA DE "CHARGES" — OBRIGATÓRIO PARA GERAÇÃO DO PIX
    // CADA CHARGE CONTÉM:
    //  - amount.value  (valor total)
    //  - payment_method.type = "PIX"
    // =========================================================================
    @NotNull(message = "charges cannot be null")
    @Size(min = 1, message = "at least one charge must be provided")
    @JsonProperty("charges")
    @Valid
    private List<PagBankChargeRequestDto> charges;
}
