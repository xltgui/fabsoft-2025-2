package br.univille.pagfut.domain.user;

import br.univille.pagfut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserEntity save(UserEntity userEntity){
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        return repository.save(userEntity);
    }

    public UserEntity findByUsername(String username){
        return repository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Username not found with this username"));
    }

    public void delete(Long id){
        repository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found with this username"));
        repository.deleteById(id);
    }

    public  UserEntity getLoggedUser(){
        UserDetails details =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findByUsername(details.getUsername())
                            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
