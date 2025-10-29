package br.univille.pagfut.api.user;

public record UserResponse(
        Long id,
        String username,
        String email
) {
}
