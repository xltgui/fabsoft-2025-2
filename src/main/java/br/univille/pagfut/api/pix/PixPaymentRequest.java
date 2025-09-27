package br.univille.pagfut.api.pix;

import jakarta.validation.constraints.NotBlank;

public record PixPaymentRequest(
        @NotBlank(message = "Field required!")
        String amount
) {
}
