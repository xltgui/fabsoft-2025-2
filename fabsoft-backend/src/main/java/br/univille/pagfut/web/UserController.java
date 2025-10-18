package br.univille.pagfut.web;

import br.univille.pagfut.api.user.UserLoginRequest;
import br.univille.pagfut.api.user.UserRegisterRequest;
import br.univille.pagfut.api.user.UserResponse;
import br.univille.pagfut.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(mapper.toDto(userService.login(request.email(), request.password())));
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> save(@Valid @RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(userService.register(mapper.toEntity(request))));
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmRegistration(@RequestParam("token") String token){
        userService.confirmUser(token);

        URI redirectUri = URI.create(frontendBaseUrl + "users/register/confirmation-success");

        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(redirectUri)
                .build();
    }
}
