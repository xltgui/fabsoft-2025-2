package br.univille.pagfut.api.match;

import br.univille.pagfut.domain.match.SoccerPlace;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchCreationRequest(
        @NotNull(message = "Field required!")
        LocalDate date,

        @NotNull(message = "Field required!")
        LocalTime startTime,

        @NotNull(message = "Field required!")
        LocalTime endTime,

        @NotNull(message = "Field required!")
        SoccerPlace place
) {
}
