package br.com.educelulares.backend.dto;

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
    // IDENTIFICADOR ÚNICO DO PEDIDO NA SUA APLICAÇÃO.
    // SERVE PARA RELACIONAR A RESPOSTA DO PAGBANK COM O PEDIDO LOCAL.
    // EXEMPLO: "PEDIDO-123", "VENDA-2025-01"
    // -------------------------------------------------------------------------
    private String reference_Id;

    // -------------------------------------------------------------------------
    // INFORMAÇÕES DO CLIENTE (NOME, EMAIL, CPF, ENDEREÇO ETC.)
    // REPRESENTADO PELO DTO PagBankCustomerDto
    // -------------------------------------------------------------------------
    private PagBankCustomerDto customer;

    // -------------------------------------------------------------------------
    // LISTA DE ITENS DO PEDIDO
    // CADA ITEM CONTÉM: NOME, QUANTIDADE E VALOR UNITÁRIO
    // REPRESENTADO PELO DTO PagBankItemDto
    // -------------------------------------------------------------------------
    private List<PagBankItemDto> items;

    // -------------------------------------------------------------------------
    // INFORMAÇÕES DE ENTREGA / FRETE DO PEDIDO
    // REPRESENTADO PELO DTO PagBankShippingDto
    // -------------------------------------------------------------------------
    private PagBankShippingDto shipping;

    // -------------------------------------------------------------------------
    // URLS QUE O PAGBANK IRÁ NOTIFICAR SOBRE ALTERAÇÃO DO STATUS DO PAGAMENTO
    // EXEMPLO: https://meusite.com/pagbank/webhook
    // -------------------------------------------------------------------------
    private List<String> notification_urls;
}


