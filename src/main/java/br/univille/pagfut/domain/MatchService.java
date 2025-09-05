package br.univille.pagfut.domain;

import br.univille.pagfut.api.MatchCreationRequest;
import br.univille.pagfut.repository.MatchAssignmentRepository;
import br.univille.pagfut.repository.SoccerMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchAssignmentRepository matchAssignmentRepository;
    private final SoccerMatchRepository soccerMatchRepository;

    public void create(MatchCreationRequest request){
        SoccerMatch soccerMatch = new SoccerMatch();

        soccerMatch.setDate(request.date());
        soccerMatch.setStartTime(request.startTime());
        soccerMatch.setEndTime(request.endTime());

        soccerMatch.setMatchCode(generateMatchCode(6));

    }

    public String generateMatchCode(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder code = new StringBuilder();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(chars.length());
            code.append(chars.charAt(index));
        }

        System.out.println("CODE=" + code);
        return code.toString();
    }
}
