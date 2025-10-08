package br.univille.pagfut.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public record ResponseError(int status, String message, List<MyFieldError> errors) {
    public static ResponseError defaultResponse(String message) {
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ResponseError notFoundResponse(String message) {
        return new ResponseError(HttpStatus.NOT_FOUND.value(), message, List.of());
    }

    public static ResponseError conflict(String message) {
        return new ResponseError(HttpStatus.CONFLICT.value(), message, List.of());
    }
}
