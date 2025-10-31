package br.univille.pagfut.api.user;

public record LoginResponse(
        String token,
		UserResponse user
) {
}
