package br.univille.pagfut.api.user;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record   UserRequest(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        Set<String> roles
) {
}
