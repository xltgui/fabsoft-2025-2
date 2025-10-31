package br.univille.pagfut.api.soccerPlayer;

public record SoccerPlayerResponse(
		Long id,
        String nickname,
        Boolean paid
) {
}
