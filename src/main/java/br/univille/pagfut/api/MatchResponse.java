package br.univille.pagfut.api;


import br.univille.pagfut.domain.SoccerPlace;
import br.univille.pagfut.domain.SoccerPlayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MatchResponse(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        SoccerPlace place,
        String matchCode,
        List<SoccerPlayerResponse> soccerPlayers
) {
}
