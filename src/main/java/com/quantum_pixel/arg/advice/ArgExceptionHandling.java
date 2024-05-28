package com.quantum_pixel.arg.advice;


import com.quantum_pixel.arg.hotel.exception.PastDateException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ControllerAdvice
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

}
