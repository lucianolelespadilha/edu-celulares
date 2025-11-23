package br.com.educelulares.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// -----------------------------------------------------------------------------
// DTO RESPONSÁVEL POR REPRESENTAR A RESPOSTA DE CRIAÇÃO DE PEDIDO NO PAGBANK
// CONTÉM O ID DO PEDIDO, REFERÊNCIA INTERNA DO SISTEMA, STATUS ATUAL
// E A LISTA DE COBRANÇAS (CHARGES), QUE INCLUEM LINKS E QR CODE DE PAGAMENTO
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBakOrderResponseDto {

    // IDENTIFICADOR DO PEDIDO GERADO PELO PAGBANK
    private String id;

    // IDENTIFICADOR DO PEDIDO NO SEU SISTEMA (EX: "ORDER-123")
    private String reference_id;

    // STATUS ATUAL DO PEDIDO (EX: "CREATED", "WAITING_PAYMENT", "PAID")
    private String status;

    // LISTA DE COBRANÇAS GERADAS PARA O PEDIDO (LINKS, QR CODE, ETC.)
    private List<PagBankLinkDto> charges;
}
