package br.univille.pagfut.api.pix;

import java.math.BigDecimal;

public record PixPaymentRequest(
        String key,
        BigDecimal amount,
        String receiver,
        String description
) {
}
