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
 * DTO PRINCIPAL ENVIADO PARA O PAGBANK PARA CRIAR UMA ORDEM PIX.
 * CONTÉM REFERÊNCIA INTERNA, CLIENTE, ITENS, ENTREGA E URLs DE NOTIFICAÇÃO.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankOrderRequestDto {

    // -------------------------------------------------------------------------
    // ID DO PEDIDO NA SUA APLICAÇÃO
    // MAPEADO CORRETAMENTE PARA reference_id NO JSON
    // -------------------------------------------------------------------------
    @NotBlank(message = "reference_id is required")
    @JsonProperty("reference_id")
    private String referenceId;

    // -------------------------------------------------------------------------
    // DADOS DO CLIENTE: NOME, EMAIL, TELEFONE, CPF
    // -------------------------------------------------------------------------
    @NotNull(message = "customer is required")
    @Valid
    private PagBankCustomerDto customer;

    // -------------------------------------------------------------------------
    // LISTA DOS ITENS DO PEDIDO
    // -------------------------------------------------------------------------
    @NotNull(message = "items list cannot be null")
    @Size(min = 1, message = "order must contain at least 1 item")
    @Valid
    private List<PagBankItemDto> items;

    // -------------------------------------------------------------------------
    // DADOS DE ENTREGA (ENDEREÇO + VALOR)
    // -------------------------------------------------------------------------
    @NotNull(message = "shipping info is required")
    @Valid
    private PagBankShippingDto shipping;

    // -------------------------------------------------------------------------
    // URL QUE O PAGBANK VAI USAR PARA ENVIAR WEBHOOKS
    // DEVE SER ENVIADA EXATAMENTE COMO notification_urls NO JSON
    // -------------------------------------------------------------------------
    @NotNull(message = "notification_urls cannot be null")
    @Size(min = 1, message = "at least one notification URL must be provided")
    @JsonProperty("notification_urls")
    private List<
            @Pattern(
                    regexp = "https?://.*",
                    message = "each notification URL must be a valid http/https address"
            )
                    String> notificationUrls;
}
