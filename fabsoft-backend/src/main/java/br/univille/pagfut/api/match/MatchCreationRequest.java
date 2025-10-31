package br.univille.pagfut.api.match;

import br.univille.pagfut.domain.match.SoccerPlace;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record MatchCreationRequest(
        @NotNull(message = "Field required!")
		LocalDate date,

        @NotNull(message = "Field required!")
        LocalDateTime startTime,

        @NotNull(message = "Field required!")
		LocalDateTime endTime,

        @NotNull(message = "Field required!")
        SoccerPlace place
) {
}
