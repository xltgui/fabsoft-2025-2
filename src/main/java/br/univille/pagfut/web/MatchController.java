package br.univille.pagfut.web;

import br.univille.pagfut.api.match.MatchCreationRequest;
import br.univille.pagfut.domain.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchMapper mapper;
    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MatchCreationRequest request) {
        var response = mapper.toDto(matchService.create(mapper.toEntity(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(mapper.toDtoList(matchService.listAll()));
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

}
