package br.com.educelulares.backend.dto;

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

    // -------------------------------------------------------------------------
    // ENDEREÇO COMPLETO DO CLIENTE.
    // OBJETO PagBankAddressDto JÁ PADRONIZADO NO FORMATO EXATO DO PAGBANK
    // -------------------------------------------------------------------------
    private PagBankAddressDto address;

    // -------------------------------------------------------------------------
    // VALOR DO FRETE EM CENTAVOS.
    // EXEMPLO: R$ 12,50 → enviar "1250"
    // -------------------------------------------------------------------------
    private Integer amount;
}
