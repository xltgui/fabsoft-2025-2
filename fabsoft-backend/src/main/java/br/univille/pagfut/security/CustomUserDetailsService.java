package br.univille.pagfut.security;

import br.univille.pagfut.domain.user.UserEntity;
import br.univille.pagfut.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity userEntity = service.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }

        return User.
                builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .build();
    }
}
