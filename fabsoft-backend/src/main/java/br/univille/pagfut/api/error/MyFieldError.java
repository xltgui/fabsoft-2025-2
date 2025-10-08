package br.univille.pagfut.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record MyFieldError(String field, String message) {
}
