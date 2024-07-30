package com.quantum_pixel.arg.advice;


import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.v1.web.model.ErrorDTO;
import com.quantum_pixel.arg.v1.web.model.ErrorsDTO;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Log4j2
public class ArgExceptionHandling {

    @ExceptionHandler(PastDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidArgument(PastDateException pastDateException) {
        Arrays.stream(pastDateException.getStackTrace()).forEach(System.out::println);
        return ResponseEntity.badRequest().body(pastDateException.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ValidationException pastDateException) {
        return ResponseEntity.badRequest().body(pastDateException.getMessage());
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    ResponseEntity<ErrorsDTO> handleResponseStatusException(
            ResponseStatusException ex, WebRequest req) {
        String message = ex.getMessage();
        log.error("Response status exception '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(ex.getStatusCode().value())
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorType(ErrorDTO.ErrorTypeEnum.FUNCTIONAL)
                                                        .errorMessage(message)
                                                        .errorStacktrace(getStacktrace(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorsDTO> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest req) {
        String message = ex.getMessage();
        log.error("Invalid argument exception for path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(BAD_REQUEST.value())
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorMessage(BAD_REQUEST.getReasonPhrase() + ": " + message)
                                                        .errorStacktrace(getStacktrace(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(value = FORBIDDEN)
    public ResponseEntity<ErrorsDTO> accessDeniedException(AccessDeniedException ex, WebRequest req) {
        String message = ex.getMessage();
        log.error("Access denied to path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(FORBIDDEN.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(FORBIDDEN.getReasonPhrase() + ": " + message)
                                                        .errorStacktrace(getStacktrace(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorsDTO> missingServletRequestParameterException(
            MissingServletRequestParameterException ex, WebRequest req) {
        String message = ex.getMessage();
        log.error(
                "Missing request parameter for the path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(BAD_REQUEST.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage("Missing parameter " + ex.getParameterName() + ": " + message)
                                                        .errorStacktrace(getStacktrace(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorsDTO> unknownException(Exception ex, WebRequest req) {
        log.error("Unexpected exception", ex);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(INTERNAL_SERVER_ERROR.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(
                                                                INTERNAL_SERVER_ERROR.getReasonPhrase()
                                                                        + ": "
                                                                        + ex.getMessage())
                                                        .errorStacktrace(getStacktrace(ex))
                                                        .build()))
                                .build());
    }

    private static String getStacktrace(Throwable throwable) {
        var sw = new StringWriter();
        var pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
