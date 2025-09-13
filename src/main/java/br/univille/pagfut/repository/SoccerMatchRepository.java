package br.univille.pagfut.repository;

import br.univille.pagfut.domain.SoccerMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoccerMatchRepository extends JpaRepository<SoccerMatch, Long> {
    Optional<SoccerMatch> findByMatchCode(String matchCode);
}
