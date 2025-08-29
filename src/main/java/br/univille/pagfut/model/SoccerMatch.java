package br.univille.pagfut.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

public class SoccerMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matchId;
    private String paymentKey;
    private User admin;
    private List<SoccerPlayer> users;
}
