package br.com.educelulares.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO UTILIZADO PARA RECEBER A RESPOSTA DO PAGBANK APÓS CRIAR UMA ORDEM.
 * MAPEIA EXATAMENTE A ESTRUTURA JSON ENVIADA PELO PAGBANK.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankOrderResponseDto {

    // -------------------------------------------------------------------------
    // ID GERADO PELO PAGBANK PARA A ORDEM
    // -------------------------------------------------------------------------
    @JsonProperty("id")
    private String id;

    // -------------------------------------------------------------------------
    // ID INTERNO QUE FOI ENVIADO COMO reference_id NA REQUISIÇÃO
    // -------------------------------------------------------------------------
    @JsonProperty("reference_id")
    private String referenceId;

    // -------------------------------------------------------------------------
    // STATUS DO PAGAMENTO (CREATED, WAITING_PAYMENT, PAID, EXPIRED, CANCELED)
    // Pode retornar null logo após a criação da ordem PIX.
    // -------------------------------------------------------------------------
    @JsonProperty("status")
    private String status;

    // -------------------------------------------------------------------------
    // LISTA DE COBRANÇAS (CHARGES)
    // Cada cobrança contém o método de pagamento e o QR Code PIX.
    // -------------------------------------------------------------------------
    @JsonProperty("charges")
    @Valid
    private List<PagBankChargeResponseDto> charges;

    // -------------------------------------------------------------------------
    // DATA DE CRIAÇÃO DA ORDEM NO PAGBANK
    // -------------------------------------------------------------------------
    @JsonProperty("created_at")
    private String createdAt;

    // -------------------------------------------------------------------------
    // DADOS DO CLIENTE (NOME, EMAIL, CPF)
    // -------------------------------------------------------------------------
    @JsonProperty("customer")
    private PagBankCustomerDto customer;

    // -------------------------------------------------------------------------
    // LISTA DE ITENS DA ORDEM RETORNADOS PELO PAGBANK
    // -------------------------------------------------------------------------
    @JsonProperty("items")
    private List<PagBankItemDto> items;

    // -------------------------------------------------------------------------
    // INFORMAÇÕES DE ENTREGA (ENDEREÇO)
    // -------------------------------------------------------------------------
    @JsonProperty("shipping")
    private PagBankShippingDto shipping;

    // -------------------------------------------------------------------------
    // URLS DE NOTIFICAÇÃO QUE O PAGBANK USARÁ PARA ENVIAR WEBHOOKS
    // -------------------------------------------------------------------------
    @JsonProperty("notification_urls")
    private List<String> notificationUrls;

    // -------------------------------------------------------------------------
    // LINKS DE AÇÃO RETORNADOS PELO PAGBANK (SELF, PAY, ETC.)
    // -------------------------------------------------------------------------
    @JsonProperty("links")
    private List<PagBankLinkDto> links;
}
