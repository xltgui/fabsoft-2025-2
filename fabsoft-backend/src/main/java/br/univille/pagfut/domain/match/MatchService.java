package br.univille.pagfut.domain.match;

import br.univille.pagfut.api.match.MatchCreationRequest;
import br.univille.pagfut.api.pix.PixKeySetRequest;
import br.univille.pagfut.api.pix.PixPaymentRequest;
import br.univille.pagfut.domain.pix.PixKey;
import br.univille.pagfut.domain.pix.PixQrCodeService;
import br.univille.pagfut.domain.user.UserEntity;
import br.univille.pagfut.domain.user.UserService;
import br.univille.pagfut.repository.PixKeyRepository;
import br.univille.pagfut.repository.SoccerMatchRepository;
import br.univille.pagfut.web.MatchMapper;
import br.univille.pagfut.web.exception.DuplicatedRegisterException;
import br.univille.pagfut.web.exception.InvalidFieldException;
import br.univille.pagfut.web.exception.NotFoundException;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final SoccerMatchRepository soccerMatchRepository;
    private final MatchValidator validator;

    private final PixQrCodeService qrCodeService;
    private final UserService userService;
    private final MatchMapper matchMapper;
    private final PixKeyRepository pixKeyRepository;

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

    public List<SoccerMatch> listByLoggedUser(){
        UserEntity loggedUser = userService.getLoggedUser();
        return soccerMatchRepository.findMatchesByPlayerId(loggedUser.getId());
    }

    public SoccerMatch findByMatchCode(String matchCode) {
        SoccerMatch match =  soccerMatchRepository.findByMatchCode(matchCode)
                .orElseThrow( () -> new NotFoundException("Match not found with this code!", matchCode));

        UserEntity admin = match.getAdmin();
        match.getSoccerPlayers().sort(
                Comparator
                        .comparing((SoccerPlayer p) -> p.getUserEntity().equals(admin), Comparator.reverseOrder())
                        .thenComparing((SoccerPlayer p) -> p.getUserEntity().getNickname())
        );
        return match;
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
                .ifPresent(p -> p.setPaid(p.getPaid() == false));

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

        PixKey existingKey = pixKeyRepository.findByKeyValueAndKeyType(
                pixKeySet.keyValue(), pixKeySet.keyType()).orElse(null);

        if(existingKey != null){
            existingKey.setKeyValue(pixKeySet.keyValue());
            existingKey.setKeyType(pixKeySet.keyType());
            existingKey.setRecipientName(pixKeySet.recipientName());
            existingKey.setRecipientCity(pixKeySet.recipientCity());

            match.setPixKey(existingKey);

        } else {
            PixKey newKey = new PixKey();
            newKey.setKeyValue(pixKeySet.keyValue());
            newKey.setKeyType(pixKeySet.keyType());
            newKey.setRecipientName(pixKeySet.recipientName());
            newKey.setRecipientCity(pixKeySet.recipientCity());

            match.setPixKey(newKey);
        }

        soccerMatchRepository.save(match);
    }

    public SoccerMatch setMatchQrCode(PixPaymentRequest request, String matchCode) throws WriterException, IOException {
        SoccerMatch match = validator.validateMatchAndUserOwner(matchCode);

        String payload = qrCodeService.generateStaticPayload(
                match.getPixKey().getKeyValue(),
                match.getPixKey().getKeyType(),
                match.getPixKey().getRecipientName(),
                match.getPixKey().getRecipientCity(),
                request.amount()
        );

        byte[] qrCodeBytes = qrCodeService.generateStaticQrCode(payload);

        String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);

        match.setPayload(payload);

        match.setQrCodeUrl(qrCodeBase64);

        return soccerMatchRepository.save(match);
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


    public void removePlayer(String matchCode, Long playerIdToRemove) {
        SoccerMatch match = validator.validateMatchAndUserOwner(matchCode);
        validator.isPlayerInTheMatch(match, playerIdToRemove);

        match.getSoccerPlayers().removeIf(player ->
                player.getUserEntity().getId().equals(playerIdToRemove));

        soccerMatchRepository.save(match);
    }
}
