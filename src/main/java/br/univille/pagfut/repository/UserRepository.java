package br.univille.pagfut.repository;

import br.univille.pagfut.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
