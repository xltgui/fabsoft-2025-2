package br.univille.pagfut.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "match_assignment")
public class MatchAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private SoccerMatch match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
