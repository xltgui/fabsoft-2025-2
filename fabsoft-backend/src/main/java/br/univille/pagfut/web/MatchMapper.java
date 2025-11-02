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

    @Mapping(source = "admin.nickname", target = "adminNickname") // adminNickname é o campo do DTO, source é a entity buscando o atributo nickname do admin
    @Mapping(source = "admin.id", target = "adminId")
    MatchResponse toDto(SoccerMatch match);

    List<MatchResponse> toDtoList(List<SoccerMatch> entityList);

    @Mapping(source = "userEntity.id", target = "id")
    @Mapping(source = "userEntity.nickname", target = "nickname")
    SoccerPlayerResponse toSoccerPlayerDto(SoccerPlayer soccerPlayer);

}
