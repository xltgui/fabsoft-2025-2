package br.univille.pagfut.api.user;

public record UserResponse(
        Long id,
        String nickname,
        String email
) {
}
