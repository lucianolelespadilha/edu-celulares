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
// DTO RESPONSÁVEL POR REPRESENTAR A RESPOSTA DE CRIAÇÃO DE PEDIDO NO PAGBANK
// CONTÉM O ID DO PEDIDO, REFERÊNCIA INTERNA DO SISTEMA, STATUS ATUAL
// E A LISTA DE COBRANÇAS (CHARGES), QUE INCLUEM LINKS E QR CODE DE PAGAMENTO
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankOrderResponseDto {

    // ID GERADO PELO PAGBANK PARA A ORDEM
    @NotBlank(message = "Order ID from PagBank cannot be null or blank")
    private String id;

    // ID INTERNO DA SUA APLICAÇÃO
    @NotBlank(message = "reference_id cannot be blank")
    private String reference_id;

    // STATUS DO PAGBANK (CREATED, WAITING_PAYMENT, PAID, CANCELED, EXPIRED)
    @NotBlank(message = "Order status cannot be blank")
    @Pattern(
            regexp = "CREATED|WAITING_PAYMENT|PAID|CANCELED|EXPIRED",
            message = "Invalid PagBank order status"
    )
    private String status;

    // COBRANÇAS ASSOCIADAS (LINKS, QRCODE, ETC.)
    @NotNull(message = "charges list cannot be null")
    @Size(min = 1, message = "charges list must contain at least 1 element")
    @Valid
    private List<PagBankLinkDto> charges;
}

