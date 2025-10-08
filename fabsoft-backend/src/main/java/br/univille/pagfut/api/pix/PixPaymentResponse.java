package br.univille.pagfut.api.pix;

import java.math.BigDecimal;

public record PixPaymentResponse(
        String base64qrCode,
        String payload,
        String pixKey,
        BigDecimal amount
) {
}
