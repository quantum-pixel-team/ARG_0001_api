package com.quantum_pixel.arg.hotel.advice;


import com.quantum_pixel.arg.hotel.exception.PastDateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ArgExceptionHandling {

    @ExceptionHandler(PastDateException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidArgument(){
        return ResponseEntity.badRequest().body("Something went wrong ");
    }

}
