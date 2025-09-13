package br.univille.pagfut.api.match;

import br.univille.pagfut.domain.SoccerPlace;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchCreationRequest(
        @NotNull(message = "Date is required.")
        LocalDate date,
        @NotNull(message = "Time is required.")
        LocalTime startTime,
        LocalTime endTime,
        SoccerPlace place
) {
}
