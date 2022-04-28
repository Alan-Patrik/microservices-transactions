package com.alanpatrik.bancosantander.transactions.exceptions;

import com.alanpatrik.bancosantander.transactions.http.HttpExceptionDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ExceptionController {

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<HttpExceptionDTO> handleCustomBadRequestException(
            HttpServletRequest http, CustomBadRequestException exception
    ) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .header("exception-name", exception.toString().split(":")[0])
                .header("error-code", BAD_REQUEST.getReasonPhrase())
                .header("error-message", exception.getMessage())
                .body(HttpExceptionDTO
                        .builder()
                        .timestamp(dateFormat.format(System.currentTimeMillis()))
                        .code(BAD_REQUEST.value())
                        .exceptionName(exception.toString().split(":")[0])
                        .path(http.getRequestURI())
                        .message(exception.getMessage())
                        .build());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpExceptionDTO> handleConstraintViolationException(
            HttpServletRequest http, ConstraintViolationException exception
    ) {

        var list = exception.getConstraintViolations().stream().map(constraintViolation ->
                constraintViolation.getMessage()
        ).collect(Collectors.toList());

        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .header("exception-name", exception.toString().split(":")[0])
                .header("error-code", BAD_REQUEST.getReasonPhrase())
                .header("error-message", exception.getMessage())
                .body(HttpExceptionDTO
                        .builder()
                        .timestamp(dateFormat.format(System.currentTimeMillis()))
                        .code(BAD_REQUEST.value())
                        .exceptionName(exception.toString().split(":")[0])
                        .path(http.getRequestURI())
                        .message(list.get(0))
                        .build());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<HttpExceptionDTO> handleCustomNotFoundException(
            HttpServletRequest http, CustomNotFoundException exception
    ) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .header("exception-name", exception.toString().split(":")[0])
                .header("error-code", NOT_FOUND.getReasonPhrase())
                .header("error-message", exception.getMessage())
                .body(HttpExceptionDTO
                        .builder()
                        .timestamp(dateFormat.format(System.currentTimeMillis()))
                        .code(NOT_FOUND.value())
                        .exceptionName(exception.toString().split(":")[0])
                        .path(http.getRequestURI())
                        .message(exception.getMessage())
                        .build());
    }
}
