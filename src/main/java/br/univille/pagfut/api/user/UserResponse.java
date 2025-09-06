package br.univille.pagfut.api.user;

import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String password,
        Set<String> roles
) {
}
