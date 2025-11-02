package br.univille.pagfut.repository;

import br.univille.pagfut.domain.match.SoccerMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SoccerMatchRepository extends JpaRepository<SoccerMatch, Long> {
    Optional<SoccerMatch> findByMatchCode(String matchCode);

    @Query("SELECT m FROM SoccerMatch m JOIN m.soccerPlayers p where p.userEntity.id = :userId")
    List<SoccerMatch> findMatchesByPlayerId(Long userId);
}
