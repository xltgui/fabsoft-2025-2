package br.univille.pagfut.repository;

import br.univille.pagfut.domain.match.SoccerPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoccerPlayerRepository extends JpaRepository<SoccerPlayer, Long> {
}
