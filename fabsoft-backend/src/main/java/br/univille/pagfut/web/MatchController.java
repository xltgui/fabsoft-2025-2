package br.univille.pagfut.web;

import br.univille.pagfut.api.match.MatchCreationRequest;
import br.univille.pagfut.api.match.MatchResponse;
import br.univille.pagfut.api.pix.PixKeySetRequest;
import br.univille.pagfut.api.pix.PixPaymentRequest;
import br.univille.pagfut.domain.match.MatchService;
import br.univille.pagfut.domain.match.SoccerMatch;
import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("match")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MatchController {
    private final MatchMapper mapper;
    private final MatchService matchService;


    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MatchCreationRequest request) {
        var response = mapper.toDto(matchService.create(mapper.toEntity(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(mapper.toDtoList(matchService.listAll()));
    }

    @GetMapping("/show/{matchCode}")
    public ResponseEntity<MatchResponse> listByMatchCode(@PathVariable String matchCode) {
        return ResponseEntity.ok(mapper.toDto(matchService.findByMatchCode(matchCode)));
    }

    @GetMapping("/join")
    public ResponseEntity<?> joinMatch(@RequestParam String matchCode) {
        matchService.joinMatch(matchCode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/leave/{matchCode}")
    public ResponseEntity<?> leaveMatch(@PathVariable String matchCode) {
        matchService.leaveMatch(matchCode);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/updatePayment/{matchCode}/{playerId}")
    public ResponseEntity<?> updatePayment(@PathVariable String matchCode, @PathVariable Long playerId) {
        matchService.updatePayment(matchCode, playerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{matchCode}")
    public ResponseEntity<?> updateMatchInfo(@Valid @RequestBody MatchCreationRequest request, @PathVariable String matchCode) {
        matchService.updateMatchInfo(matchCode, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pixkey/{matchCode}")
    public ResponseEntity<?> setPixKey(@Valid @RequestBody PixKeySetRequest request, @PathVariable String matchCode) {
        matchService.setPixInfo(request, matchCode);
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/generateQrCode/{matchCode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generateQrCode(@Valid @RequestBody PixPaymentRequest request, @PathVariable String matchCode) throws WriterException, IOException {
        return ResponseEntity.ok().body(matchService.setMatchQrCode(request, matchCode));
    }


}
