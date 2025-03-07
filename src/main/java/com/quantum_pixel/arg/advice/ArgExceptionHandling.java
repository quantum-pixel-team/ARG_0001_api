package com.quantum_pixel.arg.advice;


import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.v1.web.model.ErrorDTO;
import com.quantum_pixel.arg.v1.web.model.ErrorsDTO;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Log4j2
public class ArgExceptionHandling {

    @Value("${error.include-stacktrace:false}")
    private boolean includeStacktrace;

    private String getStacktraceIfNeeded(Throwable ex) {
        return includeStacktrace ? getStacktrace(ex) : null;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        String errorMessage = "Invalid date format. Please use the correct format, such as 'yyyy-MM-dd'T'HH:mm:ss.SSSXXX' for OffsetDateTime.";
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(PastDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidArgument(PastDateException pastDateException) {
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
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
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
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
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
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
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
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorsDTO> unknownException(Exception ex) {
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
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ErrorsDTO> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest req) {
        String message = "Request method '" + ex.getMethod() + "' is not supported for this endpoint.";
        log.error("HTTP request method not supported for path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(message)
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorsDTO> handleNoResourceFound(NoResourceFoundException ex, WebRequest req) {
        String message = "No static resource found.";
        log.error("No static resource found for path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(HttpStatus.NOT_FOUND.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(message)
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorsDTO> handleMissingRequestHeader(MissingRequestHeaderException ex, WebRequest req) {
        String message = "Required request header '" + ex.getHeaderName() + "' is not present.";
        log.error("Missing request header for path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(HttpStatus.BAD_REQUEST.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(message)
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ErrorsDTO> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, WebRequest req) {
        String message = "Media type '" + ex.getContentType() + "' is not supported.";
        log.error("Unsupported media type for path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(message)
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ErrorsDTO> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, WebRequest req) {
        String message = "Media type not acceptable.";
        log.error("Media type not acceptable for path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(HttpStatus.NOT_ACCEPTABLE.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(message)
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorsDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest req) {
        String message = "Request body is not readable.";
        log.error("Request body not readable for path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(HttpStatus.BAD_REQUEST.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(message)
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
                                                        .build()))
                                .build());
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorsDTO> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, WebRequest req) {
        String message = "Response body is not writable.";
        log.error("Response body not writable for path '{}' : {}", req.getContextPath(), message, ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorsDTO.builder()
                                .errors(
                                        List.of(
                                                ErrorDTO.builder()
                                                        .timestamp(OffsetDateTime.now())
                                                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                        .errorType(ErrorDTO.ErrorTypeEnum.TECHNICAL)
                                                        .errorLevel(ErrorDTO.ErrorLevelEnum.ERROR)
                                                        .errorMessage(message)
                                                        .errorStacktrace(getStacktraceIfNeeded(ex))
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
