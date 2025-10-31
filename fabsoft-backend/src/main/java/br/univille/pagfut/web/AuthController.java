package br.univille.pagfut.web;

import br.univille.pagfut.api.user.LoginResponse;
import br.univille.pagfut.api.user.UserLoginRequest;
import br.univille.pagfut.domain.auth.TokenService;
import br.univille.pagfut.domain.user.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
	private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request) {
        // 1. Cria o objeto de autenticação que será enviado ao AuthenticationManager.
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        // 2. Realiza a autenticação. Se for bem-sucedida, retorna um objeto Authentication.
        // O Spring Security irá chamar seu CustomUserDetailsService internamente.
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Obtém o UserEntity (Principal) a partir da autenticação bem-sucedida.
        var user = (UserEntity) auth.getPrincipal();

        // 4. Gera o Token JWT usando o TokenService e o UserEntity.
        String token = tokenService.generateToken(user);

        // 5. Retorna o Token JWT encapsulado no DTO de resposta.
        return ResponseEntity.ok(new LoginResponse(token, userMapper.toDto(user)));
    }
}
