package br.univille.pagfut.api.pix;

import br.univille.pagfut.domain.pix.KeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PixKeySetRequest (
    @NotBlank(message = "Field required!")
    String keyValue,

    @NotNull(message = "Field required!")
    KeyType keyType,

    @NotBlank(message = "Field required!")
    String recipientName,

    @NotBlank(message = "Field required!")
    String recipientCity
){

}
