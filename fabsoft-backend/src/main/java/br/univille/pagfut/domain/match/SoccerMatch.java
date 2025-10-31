package br.univille.pagfut.domain.match;

import br.univille.pagfut.domain.pix.PixKey;
import br.univille.pagfut.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@Table(name = "soccer_match")
public class SoccerMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String matchCode;
    private String paymentKey;
    private String payload;

    @OneToOne(cascade =  CascadeType.ALL)
    private PixKey pixKey;

    @Enumerated(EnumType.STRING)
    private SoccerPlace place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private UserEntity admin;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoccerPlayer> soccerPlayers = new ArrayList<>();
}
