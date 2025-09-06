package br.univille.pagfut.web;

import br.univille.pagfut.api.MatchCreationRequest;
import br.univille.pagfut.api.MatchResponse;
import br.univille.pagfut.api.SoccerPlayerResponse;
import br.univille.pagfut.domain.SoccerMatch;
import br.univille.pagfut.domain.SoccerPlayer;
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
