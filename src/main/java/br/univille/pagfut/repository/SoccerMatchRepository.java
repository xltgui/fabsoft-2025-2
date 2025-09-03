package br.univille.pagfut.repository;

import br.univille.pagfut.domain.SoccerMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoccerMatchRepository extends JpaRepository<SoccerMatch, Long> {
}
