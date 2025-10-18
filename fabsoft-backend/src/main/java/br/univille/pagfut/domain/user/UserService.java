package br.univille.pagfut.domain.user;

import br.univille.pagfut.repository.UserRepository;
import br.univille.pagfut.web.exception.DuplicatedRegisterException;
import br.univille.pagfut.web.exception.InvalidConfirmationTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    public UserEntity save(UserEntity userEntity){
        if(repository.findByEmail(userEntity.getEmail()).isPresent()){
            throw new DuplicatedRegisterException("User already registred");
        }
        userEntity.setUsername(userEntity.getUsername());
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));

        String token = UUID.randomUUID().toString();
        System.out.println("TOKEN=" + token);
        userEntity.setConfirmationToken(token);
        userEntity.setEnabled(false);

        UserEntity savedUser = repository.save(userEntity);
        emailService.sendConfirmationEmail(savedUser.getEmail(), token);

        return repository.save(userEntity);
    }

    public void confirmUser(String token){
        UserEntity user = repository.findByConfirmationToken(token)
                .orElseThrow(() -> new InvalidConfirmationTokenException("Confirmation token expired or invalid"));

        if (user.isEnabled()) {
            throw new InvalidConfirmationTokenException("User already enabled, no need to confirm again");
        }
        user.setEnabled(true);
        user.setConfirmationToken(null);

        repository.save(user);
    }

    public UserEntity findByEmail(String username){
        return repository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Username not found with this email"));
    }

    public void delete(Long id){
        repository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found with this ID"));
        repository.deleteById(id);
    }

    public UserEntity getLoggedUser(){
        UserDetails details =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findByEmail(details.getUsername())
                            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
