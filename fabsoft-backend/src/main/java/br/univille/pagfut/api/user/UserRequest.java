package br.univille.pagfut.api.user;

import jakarta.validation.constraints.NotBlank;

public record   UserRequest(
        @NotBlank(message = "Field required!")
        String username,
        @NotBlank(message = "Field required!")
        String password
) {
}
