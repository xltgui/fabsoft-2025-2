package br.univille.pagfut.domain;

import br.univille.pagfut.api.pix.PixPaymentRequest;
import br.univille.pagfut.domain.user.UserEntity;
import br.univille.pagfut.domain.user.UserService;
import br.univille.pagfut.repository.SoccerMatchRepository;
import br.univille.pagfut.repository.SoccerPlayerRepository;
import br.univille.pagfut.repository.UserRepository;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final SoccerMatchRepository soccerMatchRepository;
    private final SoccerPlayerRepository soccerPlayerRepository;
    private final UserRepository userRepository;

    private final PixQrCodeService qrCodeService;
    private final UserService userService;

    public SoccerMatch create(SoccerMatch match){
        match.setMatchCode(generateMatchCode(6));

        UserEntity admin = userService.getLoggedUser();

        match.setAdmin(admin);

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


    public void joinMatch(String matchCode) {
        SoccerMatch match = findMatch(matchCode);


        if(isPlayerAlreadyJoined(match)){
            throw new RuntimeException("You are already in this match");
        }

        SoccerPlayer soccerPlayer = new SoccerPlayer();

        soccerPlayer.setUserEntity(userService.getLoggedUser());
        soccerPlayer.setMatch(match);
        soccerPlayer.setPaid(false);
        match.getSoccerPlayers().add(soccerPlayer);
        soccerMatchRepository.save(match);
    }

    public void leaveMatch(String matchCode) {
        SoccerMatch match = findMatch(matchCode);

         SoccerPlayer playerToRemove = match.getSoccerPlayers().stream()
                         .filter(p -> p.getUserEntity().equals(userService.getLoggedUser()))
                                 .findFirst()
                                         .orElseThrow(() -> new UsernameNotFoundException("You are not in this match"));

        match.getSoccerPlayers().remove(playerToRemove);
        soccerMatchRepository.save(match);
    }

    public void updatePayment(String matchCode, Long playerId){
        SoccerMatch match = findMatch(matchCode);

        match.getSoccerPlayers().stream()
                .filter(p -> p.getUserEntity().getId().equals(playerId))
                .findFirst()
                .ifPresent(p -> p.setPaid(true));

        soccerMatchRepository.save(match);
    }

    private boolean isPlayerAlreadyJoined(SoccerMatch match) {
        return match.getSoccerPlayers().stream()
                .anyMatch(player -> player.getUserEntity().equals(userService.getLoggedUser()));
    }

    public String generateBrCode(String pixKey, BigDecimal amount, String matchCode) {
        SoccerMatch match = soccerMatchRepository.findByMatchCode(matchCode)
                .orElseThrow(() -> new UsernameNotFoundException("Match not found with this code!"));

        return null;
    }

    public String setMatchQrCode(PixPaymentRequest request, String matchCode) throws WriterException, IOException {
        SoccerMatch match = findMatch(matchCode);
        String brCode = qrCodeService.generatePixQrCode(request);
        match.setBrCode(brCode);
        soccerMatchRepository.save(match);
        return qrCodeService.generatePixQrCode(request);
    }


    public String generateMatchCode(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder code = new StringBuilder();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(chars.length());
            code.append(chars.charAt(index));
        }

        return code.toString();
    }

    public SoccerMatch findMatch(String matchCode){
        return soccerMatchRepository.findByMatchCode(matchCode)
                .orElseThrow(() -> new UsernameNotFoundException("Match not found with this code!"));
    }
}
