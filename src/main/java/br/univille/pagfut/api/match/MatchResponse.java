package br.univille.pagfut.api.match;


import br.univille.pagfut.api.soccerPlayer.SoccerPlayerResponse;
import br.univille.pagfut.domain.SoccerPlace;

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
