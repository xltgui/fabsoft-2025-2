package br.univille.pagfut.web.exception;

import lombok.Getter;

public class InvalidFieldException extends RuntimeException {
  @Getter
  private final String field;

    public InvalidFieldException(String message, String field) {
        super(message);
        this.field = field;
    }
}
