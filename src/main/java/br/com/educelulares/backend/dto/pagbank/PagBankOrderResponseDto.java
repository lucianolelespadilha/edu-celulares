package br.com.educelulares.backend.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagBankOrderResponseDto {

    /** ID DO PEDIDO NO PAGBANK */
    @JsonProperty("id")
    private String id;

    /** reference_id enviado no request */
    @JsonProperty("reference_id")
    private String referenceId;

    /**
     * STATUS DA ORDEM
     * OBS: NO PIX NORMALMENTE VEM NULL
     */
    @JsonProperty("status")
    private String status;

    /** QR Codes do PIX */
    @JsonProperty("qr_codes")
    private List<PagBankQrCodeResponseDto> qrCodes;

    /**
     * CHARGES — AQUI ESTÁ O STATUS REAL DO PIX
     */
    @JsonProperty("charges")
    private List<PagBankChargeDto> charges;
}
