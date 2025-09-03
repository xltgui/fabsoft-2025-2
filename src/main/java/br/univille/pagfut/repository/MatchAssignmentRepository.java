package br.univille.pagfut.repository;

import br.univille.pagfut.domain.MatchAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchAssignmentRepository extends JpaRepository<MatchAssignment, Long> {
}
