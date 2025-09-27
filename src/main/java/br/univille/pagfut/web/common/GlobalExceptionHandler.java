package br.univille.pagfut.web.common;

import br.univille.pagfut.api.error.MyFieldError;
import br.univille.pagfut.api.error.ResponseError;
import br.univille.pagfut.web.exception.DuplicatedRegisterException;
import br.univille.pagfut.web.exception.ForbiddenOperationException;
import br.univille.pagfut.web.exception.InvalidFieldException;
import br.univille.pagfut.web.exception.NotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();

        List<MyFieldError> errorsList = fieldErrors.stream().
                map(fe -> new MyFieldError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error", errorsList);

    }

    @ExceptionHandler(DuplicatedRegisterException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError DuplicatedRegisterException(DuplicatedRegisterException e) {
        return ResponseError.conflict(e.getMessage());
    }


    @ExceptionHandler(ForbiddenOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError handleForbiddenOperationException(ForbiddenOperationException e){
        return ResponseError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleInvalidFieldException(InvalidFieldException e){
        return new ResponseError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error",
                List.of(new MyFieldError(e.getField(), e.getMessage()))
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleNotFoundException(NotFoundException e){
        if(e.getField() == null) {
            return ResponseError.notFoundResponse(e.getMessage());
        }else{
            return new ResponseError(
                    HttpStatus.NOT_FOUND.value(),
                    "Validation error",
                    List.of(new MyFieldError(e.getField(), e.getMessage()))
            );
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError handleAccessDeniedException(AccessDeniedException e){
        return new ResponseError(HttpStatus.FORBIDDEN.value(), "Access denied", List.of());
    }

    /*@ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleUnhandledErrorsException(RuntimeException e) {
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error found, try contacting the staff team", List.of());
    }*/

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String errorMessage = "Malformed JSON request. Please check the data types, especially for 'role'.";
        String fieldError = "";

        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) e.getCause();
            if (cause.getTargetType().isEnum()) {
                fieldError = cause.getPath().get(cause.getPath().size() - 1).getFieldName();

                errorMessage = String.format("Invalid values. Accepted values are: %s.",
                        Arrays.toString(cause.getTargetType().getEnumConstants()));
            }
        }

        return new ResponseError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                List.of(new MyFieldError(fieldError, errorMessage))
        );
    }
}
