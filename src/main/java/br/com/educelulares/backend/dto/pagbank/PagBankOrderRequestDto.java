package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO PRINCIPAL RESPONSÁVEL POR REPRESENTAR O PEDIDO A SER ENVIADO AO PAGBANK.
 *
 * ESTE OBJETO É SERIALIZADO PARA O JSON DO ENDPOINT:
 *      POST /orders
 *
 * CAMPOS OBRIGATÓRIOS:
 *  - reference_id: IDENTIFICADOR ÚNICO DA SUA APLICAÇÃO (USAMOS O ID DA ORDER LOCAL)
 *  - customer: DADOS DO COMPRADOR
 *  - items: LISTA DOS ITENS DA COMPRA
 *  - qr_codes: LISTA DE QR CODES (QUANDO USAMOS QR_CODE)
 *  - shipping: ENDEREÇO / DADOS DE ENTREGA
 *  - notification_urls: URL(S) PARA RECEBER O WEBHOOK DO PAGBANK
 *
 * OBSERVAÇÃO:
 * ESTE FORMATO USA "qr_codes" (LISTA DE PagBankQrCodeRequestDto), NÃO "charges".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankOrderRequestDto {

    /** IDENTIFICADOR ÚNICO DO PEDIDO NA SUA APLICAÇÃO */
    @NotBlank(message = "O REFERENCE_ID É OBRIGATÓRIO")
    @JsonProperty("reference_id")
    private String referenceId;

    /** OBJETO COM OS DADOS DO CLIENTE QUE IRÁ PAGAR */
    @NotNull(message = "O OBJETO CUSTOMER É OBRIGATÓRIO")
    @Valid
    @JsonProperty("customer")
    private PagBankCustomerDto customer;

    /** LISTA DOS ITENS DO PEDIDO */
    @NotEmpty(message = "A LISTA DE ITENS DO PEDIDO NÃO PODE ESTAR VAZIA")
    @Valid
    @JsonProperty("items")
    private List<PagBankItemDto> items;

    /**
     * LISTA DE QR CODES (FORMATO OFICIAL PARA QR PIX EM /orders).
     * CADA ENTRADA É UM PagBankQrCodeRequestDto (amount + expiration_date).
     */
    @NotNull(message = "O CAMPO qr_codes É OBRIGATÓRIO (PODE SER UMA LISTA VAZIA DEPENDENDO DO CASO)")
    @Valid
    @JsonProperty("qr_codes")
    private List<PagBankQrCodeRequestDto> qrCodes;

    /** DADOS DE ENTREGA (MESMO QUE NÃO USE ENTREGA FÍSICA, O PAGBANK PERMITE INFORMAR PARA PADRONIZAÇÃO) */
    @NotNull(message = "O OBJETO SHIPPING É OBRIGATÓRIO")
    @Valid
    @JsonProperty("shipping")
    private PagBankShippingDto shipping;

    /** URLS QUE RECEBERÃO AS NOTIFICAÇÕES (WEBHOOK) DO PAGBANK */
    @NotEmpty(message = "A LISTA DE notification_urls É OBRIGATÓRIA")
    @JsonProperty("notification_urls")
    private List<String> notificationUrls;
}
