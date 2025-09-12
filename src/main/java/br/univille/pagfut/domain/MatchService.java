package br.univille.pagfut.domain;

import br.univille.pagfut.api.MatchCreationRequest;
import br.univille.pagfut.repository.SoccerMatchRepository;
import br.univille.pagfut.repository.SoccerPlayerRepository;
import br.univille.pagfut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final SoccerMatchRepository soccerMatchRepository;
    private final SoccerPlayerRepository soccerPlayerRepository;
    private final UserRepository userRepository;

    public SoccerMatch create(SoccerMatch match){
        match.setMatchCode(generateMatchCode(6));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found for this email"));

        SoccerPlayer adminPlayer = new SoccerPlayer();
        adminPlayer.setUserEntity(admin);
        adminPlayer.setMatch(match);
        adminPlayer.setPaid(false);

        match.getSoccerPlayers().add(adminPlayer);
        return soccerMatchRepository.save(match);
    }

    public List<SoccerMatch> listAll(){
        return soccerMatchRepository.findAll();
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
