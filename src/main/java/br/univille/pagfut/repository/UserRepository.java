package br.univille.pagfut.repository;

import br.univille.pagfut.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
