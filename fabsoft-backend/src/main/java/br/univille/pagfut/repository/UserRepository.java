package br.univille.pagfut.repository;

import br.univille.pagfut.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);


    Optional<UserEntity> findByConfirmationToken(String token);
}
