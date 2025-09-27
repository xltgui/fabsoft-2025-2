package br.univille.pagfut.domain;

import br.univille.pagfut.domain.user.UserService;
import br.univille.pagfut.repository.SoccerMatchRepository;
import br.univille.pagfut.web.exception.ForbiddenOperationException;
import br.univille.pagfut.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchValidator {
    private final SoccerMatchRepository repository;
    private final UserService userService;

    public SoccerMatch validateMatchAndUserOwner(String matchCode) {
        SoccerMatch match = repository.findByMatchCode(matchCode)
                .orElseThrow(() -> new NotFoundException("Match not found with this code!", matchCode));
        System.out.println("VALIDANTING");
        // Verifica se o usuário logado é o dono da partida
        if (!userService.getLoggedUser().equals(match.getAdmin())) {
            System.out.println("NOT OWNER");
            throw new ForbiddenOperationException("User not owner of the match");
        }
        return match;
    }

    public boolean existingMatch(String matchCode) {
        return repository.findByMatchCode(matchCode).isPresent();
    }

    public void isPlayerInTheMatch(SoccerMatch match, Long playerId) {
        if(match.getSoccerPlayers().stream().noneMatch(p -> p.getId().equals(playerId))){
            throw new NotFoundException("Player not in the match", null);
        }
    }
}
