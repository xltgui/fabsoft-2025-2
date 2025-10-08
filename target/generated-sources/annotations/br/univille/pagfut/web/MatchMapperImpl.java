package br.univille.pagfut.web;

import br.univille.pagfut.api.match.MatchCreationRequest;
import br.univille.pagfut.api.match.MatchResponse;
import br.univille.pagfut.api.soccerPlayer.SoccerPlayerResponse;
import br.univille.pagfut.domain.match.SoccerMatch;
import br.univille.pagfut.domain.match.SoccerPlace;
import br.univille.pagfut.domain.match.SoccerPlayer;
import br.univille.pagfut.domain.user.UserEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-01T22:38:58-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class MatchMapperImpl implements MatchMapper {

    @Override
    public SoccerMatch toEntity(MatchCreationRequest dto) {
        if ( dto == null ) {
            return null;
        }

        SoccerMatch soccerMatch = new SoccerMatch();

        soccerMatch.setDate( dto.date() );
        soccerMatch.setStartTime( dto.startTime() );
        soccerMatch.setEndTime( dto.endTime() );
        soccerMatch.setPlace( dto.place() );

        return soccerMatch;
    }

    @Override
    public MatchResponse toDto(SoccerMatch match) {
        if ( match == null ) {
            return null;
        }

        Long id = null;
        LocalDate date = null;
        LocalTime startTime = null;
        LocalTime endTime = null;
        SoccerPlace place = null;
        String matchCode = null;
        List<SoccerPlayerResponse> soccerPlayers = null;

        id = match.getId();
        date = match.getDate();
        startTime = match.getStartTime();
        endTime = match.getEndTime();
        place = match.getPlace();
        matchCode = match.getMatchCode();
        soccerPlayers = soccerPlayerListToSoccerPlayerResponseList( match.getSoccerPlayers() );

        MatchResponse matchResponse = new MatchResponse( id, date, startTime, endTime, place, matchCode, soccerPlayers );

        return matchResponse;
    }

    @Override
    public List<MatchResponse> toDtoList(List<SoccerMatch> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MatchResponse> list = new ArrayList<MatchResponse>( entityList.size() );
        for ( SoccerMatch soccerMatch : entityList ) {
            list.add( toDto( soccerMatch ) );
        }

        return list;
    }

    @Override
    public SoccerPlayerResponse toSoccerPlayerDto(SoccerPlayer soccerPlayer) {
        if ( soccerPlayer == null ) {
            return null;
        }

        String username = null;
        Boolean paid = null;

        username = soccerPlayerUserEntityUsername( soccerPlayer );
        paid = soccerPlayer.getPaid();

        SoccerPlayerResponse soccerPlayerResponse = new SoccerPlayerResponse( username, paid );

        return soccerPlayerResponse;
    }

    protected List<SoccerPlayerResponse> soccerPlayerListToSoccerPlayerResponseList(List<SoccerPlayer> list) {
        if ( list == null ) {
            return null;
        }

        List<SoccerPlayerResponse> list1 = new ArrayList<SoccerPlayerResponse>( list.size() );
        for ( SoccerPlayer soccerPlayer : list ) {
            list1.add( toSoccerPlayerDto( soccerPlayer ) );
        }

        return list1;
    }

    private String soccerPlayerUserEntityUsername(SoccerPlayer soccerPlayer) {
        UserEntity userEntity = soccerPlayer.getUserEntity();
        if ( userEntity == null ) {
            return null;
        }
        return userEntity.getUsername();
    }
}
