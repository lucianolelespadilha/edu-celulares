package br.com.educelulares.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// -----------------------------------------------------------------------------
// DTO RESPONSÁVEL POR REPRESENTAR AS INFORMAÇÕES DE ENTREGA (SHIPPING)
// DO PEDIDO ENVIADO PARA A API DO PAGBANK. CONTÉM O ENDEREÇO COMPLETO
// E O VALOR DO FRETE ASSOCIADO À COMPRA.
// -----------------------------------------------------------------------------
// ESTE OBJETO É UTILIZADO DENTRO DO ORDER REQUEST ENVIADO AO PAGBANK
// -----------------------------------------------------------------------------
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagBankShippingDto {

    // ENDEREÇO COMPLETO PARA ENTREGA
    @NotNull(message = "Shipping address is required")
    @Valid
    private PagBankAddressDto address;

    // VALOR DO FRETE EM CENTAVOS (EX: 1250 = R$ 12,50)
    @NotNull(message = "Shipping amount is required")
    @Min(value = 0, message = "Shipping amount must be zero or positive")
    private Integer amount;
}

