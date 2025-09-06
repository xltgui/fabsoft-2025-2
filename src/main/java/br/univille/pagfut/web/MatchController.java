package br.univille.pagfut.web;

import br.univille.pagfut.api.MatchCreationRequest;
import br.univille.pagfut.domain.MatchService;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
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
        System.out.println("PLACE=" + request.place());
        var response = mapper.toDto(matchService.create(mapper.toEntity(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(mapper.toDtoList(matchService.listAll()));
    }

}
