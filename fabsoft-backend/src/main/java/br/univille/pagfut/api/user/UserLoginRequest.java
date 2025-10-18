package br.univille.pagfut.api.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "Field required!")
        String email,
        @NotBlank(message = "Field required!")
        String password
) {

}
