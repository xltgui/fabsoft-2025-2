package br.univille.pagfut.api.pix;

import java.math.BigDecimal;

public record PixPaymentRequest(
        BigDecimal amount,
        String description,
        String external_reference
) {
}
