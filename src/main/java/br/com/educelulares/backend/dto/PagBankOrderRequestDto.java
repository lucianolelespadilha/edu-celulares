package br.com.educelulares.backend.dto;

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

// -----------------------------------------------------------------------------
// DTO PRINCIPAL UTILIZADO PARA ENVIAR UM PEDIDO (ORDER) AO PAGBANK.
// ESTE OBJETO REPRESENTA EXATAMENTE A ESTRUTURA ESPERADA PELA API DE ORDERS.
// AQUI REUNIMOS: REFERÊNCIA DO PEDIDO, CLIENTE, ITENS, FRETE E WEBHOOK.
// -----------------------------------------------------------------------------
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankOrderRequestDto {

    // -------------------------------------------------------------------------
    // IDENTIFICADOR ÚNICO DO PEDIDO NO SEU SISTEMA
    // NÃO PODE SER NULO OU VAZIO
    // -------------------------------------------------------------------------
    @NotBlank(message = "reference_id is required")
    private String reference_Id;

    // -------------------------------------------------------------------------
    // INFORMAÇÕES DO CLIENTE
    // NECESSÁRIO PARA GERAÇÃO DO PEDIDO
    // -------------------------------------------------------------------------
    @NotNull(message = "customer is required")
    @Valid
    private PagBankCustomerDto customer;

    // -------------------------------------------------------------------------
    // LISTA DE ITENS - NÃO PODE SER VAZIA
    // -------------------------------------------------------------------------
    @NotNull(message = "items list cannot be null")
    @Size(min = 1, message = "order must contain at least 1 item")
    @Valid
    private List<PagBankItemDto> items;

    // -------------------------------------------------------------------------
    // INFORMAÇÕES DE ENTREGA - ENDEREÇO + VALOR
    // -------------------------------------------------------------------------
    @NotNull(message = "shipping info is required")
    @Valid
    private PagBankShippingDto shipping;

    // -------------------------------------------------------------------------
    // URLS DE NOTIFICAÇÃO DO PAGBANK
    // -------------------------------------------------------------------------
    @NotNull(message = "notification_urls cannot be null")
    @Size(min = 1, message = "at least one notification URL must be provided")
    private List<
            @Pattern(
                    regexp = "https?://.*",
                    message = "each notification URL must be a valid http/https address"
            )
                    String> notification_urls;
}
