package br.univille.pagfut.domain;

import br.univille.pagfut.api.MatchCreationRequest;
import br.univille.pagfut.repository.MatchAssignmentRepository;
import br.univille.pagfut.repository.SoccerMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchAssignmentRepository matchAssignmentRepository;
    private final SoccerMatchRepository soccerMatchRepository;

    public void create(MatchCreationRequest request){

    }
}
