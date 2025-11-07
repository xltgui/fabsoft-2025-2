package br.univille.pagfut.api.pix;

public record PixKeyResponse(
        String keyValue,
        String keyType,
        String recipientName,
        String recipientCity
) {
}
