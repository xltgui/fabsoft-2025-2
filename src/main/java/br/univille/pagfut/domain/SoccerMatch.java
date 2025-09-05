package br.univille.pagfut.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
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

    private LocalTime startTime;

    private LocalTime endTime;


    private String matchCode;
    private String paymentKey;

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MatchAssignment> assignments = new ArrayList<>();

}
