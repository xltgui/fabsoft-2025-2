package br.univille.pagfut.web;

import br.univille.pagfut.api.MatchCreationRequest;
import br.univille.pagfut.domain.MatchService;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MatchCreationRequest request) {
        matchService.create(request);
        return ResponseEntity.ok().build();
    }

}
