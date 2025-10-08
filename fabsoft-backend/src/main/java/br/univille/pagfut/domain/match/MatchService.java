package br.univille.pagfut.domain.match;

import br.univille.pagfut.api.match.MatchCreationRequest;
import br.univille.pagfut.api.pix.PixKeySetRequest;
import br.univille.pagfut.api.pix.PixPaymentRequest;
import br.univille.pagfut.domain.pix.PixKey;
import br.univille.pagfut.domain.pix.PixQrCodeService;
import br.univille.pagfut.domain.user.UserEntity;
import br.univille.pagfut.domain.user.UserService;
import br.univille.pagfut.repository.SoccerMatchRepository;
import br.univille.pagfut.web.exception.DuplicatedRegisterException;
import br.univille.pagfut.web.exception.InvalidFieldException;
import br.univille.pagfut.web.exception.NotFoundException;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final SoccerMatchRepository soccerMatchRepository;
    private final MatchValidator validator;

    private final PixQrCodeService qrCodeService;
    private final UserService userService;

    public SoccerMatch create(SoccerMatch match){
        String matchCode = generateMatchCode(6);

        while(validator.existingMatch(matchCode)){
            matchCode = generateMatchCode(6);
        }

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
            throw new DuplicatedRegisterException("You are already in this match");
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
                                         .orElseThrow(() -> new NotFoundException("You are not in this match"));

        match.getSoccerPlayers().remove(playerToRemove);
        soccerMatchRepository.save(match);
    }

    public void updatePayment(String matchCode, Long playerId){
        SoccerMatch match = validator.validateMatchAndUserOwner(matchCode);
        validator.isPlayerInTheMatch(match, playerId);

        match.getSoccerPlayers().stream()
                .filter(p -> p.getUserEntity().getId().equals(playerId))
                .findFirst()
                .ifPresent(p -> p.setPaid(true));

        soccerMatchRepository.save(match);
    }

    public void updateMatchInfo(String matchCode, MatchCreationRequest matchInfo){
        SoccerMatch match = validator.validateMatchAndUserOwner(matchCode);
        match.setDate(matchInfo.date());
        match.setStartTime(matchInfo.startTime());
        match.setEndTime(matchInfo.endTime());
        match.setPlace(matchInfo.place());
        soccerMatchRepository.save(match);
    }

    public void setPixInfo(PixKeySetRequest pixKeySet, String matchCode) {
        SoccerMatch match = validator.validateMatchAndUserOwner(matchCode);

        PixKey key = new PixKey();
        key.setKeyValue(pixKeySet.key());
        key.setKeyType(pixKeySet.keyType());
        key.setRecipientName(pixKeySet.recipientName());
        key.setRecipientCity(pixKeySet.recipientCity());
        match.setPixKey(key);
        soccerMatchRepository.save(match);
    }

    public byte[] setMatchQrCode(PixPaymentRequest request, String matchCode) throws WriterException, IOException {
        SoccerMatch match = validator.validateMatchAndUserOwner(matchCode);

        String payload = qrCodeService.generateStaticPayload(
                match.getPixKey().getKeyValue(),
                match.getPixKey().getKeyType(),
                match.getPixKey().getRecipientName(),
                match.getPixKey().getRecipientCity(),
                request.amount()
        );

        match.setPayload(payload);
        soccerMatchRepository.save(match);

        return qrCodeService.generateStaticQrCode(payload);
    }

    private String generateMatchCode(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder code = new StringBuilder();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(chars.length());
            code.append(chars.charAt(index));
        }

        return code.toString();
    }

    private SoccerMatch findMatch(String matchCode){
        return soccerMatchRepository.findByMatchCode(matchCode)
                .orElseThrow(() -> new InvalidFieldException("Match not found with this code!", matchCode));
    }

    private boolean isPlayerAlreadyJoined(SoccerMatch match) {
        return match.getSoccerPlayers().stream()
                .anyMatch(player -> player.getUserEntity().equals(userService.getLoggedUser()));
    }


}
