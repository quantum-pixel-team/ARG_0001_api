package com.quantum_pixel.arg.advice;

import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.v1.web.model.ErrorsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ArgExceptionHandlingTest {
    @InjectMocks
    private ArgExceptionHandling exceptionHandler;


    @Test
    void testHandleResponseStatusException() {
        WebRequest request = mock(WebRequest.class);
        Mockito.when(request.getContextPath()).thenReturn("/test-path");

        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        ResponseEntity<ErrorsDTO> response = exceptionHandler.handleResponseStatusException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("404 NOT_FOUND \"Resource not found\"", response.getBody().getErrors().get(0).getErrorMessage());
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        WebRequest request = mock(WebRequest.class);
        Mockito.when(request.getContextPath()).thenReturn("/test-path");

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        Mockito.when(ex.getMessage()).thenReturn("Invalid argument");

        ResponseEntity<ErrorsDTO> response = exceptionHandler.handleMethodArgumentNotValid(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Bad Request: Invalid argument", response.getBody().getErrors().get(0).getErrorMessage());
    }

    @Test
    void testAccessDeniedException() {
        WebRequest request = mock(WebRequest.class);
        Mockito.when(request.getContextPath()).thenReturn("/test-path");

        AccessDeniedException ex = new AccessDeniedException("Access denied");
        ResponseEntity<ErrorsDTO> response = exceptionHandler.accessDeniedException(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Forbidden: Access denied", response.getBody().getErrors().get(0).getErrorMessage());
    }

    @Test
    void testMissingServletRequestParameterException() {
        WebRequest request = mock(WebRequest.class);
        Mockito.when(request.getContextPath()).thenReturn("/test-path");

        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("param", "String");
        ResponseEntity<ErrorsDTO> response = exceptionHandler.missingServletRequestParameterException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Missing parameter param: Required request parameter 'param' for method parameter type String is not present", response.getBody().getErrors().get(0).getErrorMessage());
    }

    @Test
    void testUnknownException() {
        Exception ex = new Exception("Unknown error");
        ResponseEntity<ErrorsDTO> response = exceptionHandler.unknownException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Internal Server Error: Unknown error", response.getBody().getErrors().get(0).getErrorMessage());
    }
}