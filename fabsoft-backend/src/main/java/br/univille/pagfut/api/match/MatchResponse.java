package br.univille.pagfut.api.match;


import br.univille.pagfut.api.pix.PixKeyResponse;
import br.univille.pagfut.api.soccerPlayer.SoccerPlayerResponse;
import br.univille.pagfut.domain.match.SoccerPlace;
import br.univille.pagfut.domain.pix.PixKey;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record MatchResponse(
        Long id,
		Long adminId,
		String adminNickname,
		String qrCodeUrl,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
		LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		LocalDateTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		LocalDateTime endTime,
        SoccerPlace place,
        String matchCode,
		PixKeyResponse pixKeyDetails,
        List<SoccerPlayerResponse> soccerPlayers
) {
}
