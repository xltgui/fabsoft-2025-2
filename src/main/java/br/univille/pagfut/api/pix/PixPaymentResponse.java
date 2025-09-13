package br.univille.pagfut.api.pix;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PixPaymentResponse(
        String id,
        BigDecimal amount,
        @JsonProperty("qr_code_base64")
        String qrCodeBase64,
        @JsonProperty("qr_code")
        String qrCode,
        String payment_uri
) {
}
