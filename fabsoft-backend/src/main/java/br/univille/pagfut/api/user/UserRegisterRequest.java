package br.univille.pagfut.api.user;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(
        @NotBlank(message = "Field required!")
        String nickname,
        @NotBlank(message = "Field required!")
        String email,
        @NotBlank(message = "Field required!")
        String password
) {
}
