package br.univille.pagfut.api;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchCreationRequest(
        @NotNull(message = "Date is required.")
        LocalDate date,
        @NotNull(message = "Time is required.")
        LocalTime startTime,
        LocalTime endTime
) {
}
