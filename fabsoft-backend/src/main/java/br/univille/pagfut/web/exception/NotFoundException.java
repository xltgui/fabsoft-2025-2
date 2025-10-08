package br.univille.pagfut.web.exception;

import lombok.Getter;

public class NotFoundException extends RuntimeException {
    @Getter
    private String field;

    public NotFoundException(String message, String field) {
        super(message);
        this.field = field;
    }

    public NotFoundException(String message) {
        super(message);
    }

}
