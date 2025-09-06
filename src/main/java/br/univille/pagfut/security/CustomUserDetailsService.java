package br.univille.pagfut.security;

import br.univille.pagfut.domain.UserEntity;
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
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = service.findByUsername(username);

        System.out.println("UserEntity: " + userEntity);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        return User.
                builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }
}
