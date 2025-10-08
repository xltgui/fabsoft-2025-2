package br.univille.pagfut.domain.match;

import br.univille.pagfut.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "soccer_player")
@Data
public class SoccerPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referência ao jogador (a pessoa)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    // Referência à partida em que ele está participando
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private SoccerMatch match;

    private Boolean paid = false;
}