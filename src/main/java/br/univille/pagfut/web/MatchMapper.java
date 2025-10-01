package br.univille.pagfut.web;

import br.univille.pagfut.api.match.MatchCreationRequest;
import br.univille.pagfut.api.match.MatchResponse;
import br.univille.pagfut.api.soccerPlayer.SoccerPlayerResponse;
import br.univille.pagfut.domain.match.SoccerMatch;
import br.univille.pagfut.domain.match.SoccerPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(target = "id", ignore = true)
    SoccerMatch toEntity(MatchCreationRequest dto);

    MatchResponse toDto(SoccerMatch match);

    List<MatchResponse> toDtoList(List<SoccerMatch> entityList);

    @Mapping(source = "userEntity.username", target = "username")
    SoccerPlayerResponse toSoccerPlayerDto(SoccerPlayer soccerPlayer);

}
