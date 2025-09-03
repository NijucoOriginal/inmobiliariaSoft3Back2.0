package com.jsebastian.eden.EdenSys.exceptions;

import com.example.demo.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValueConflictException.class)
    public ResponseEntity<ErrorResponse> handleValueConflictException(ValueConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("ERROR",ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValueConflictException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ex.getFieldErrors().stream().map(
                        e->new ErrorResponse("ERROR",e.getField()+"->"+e.getDefaultMessage())
                ).toList()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated", ex.getMessage()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> authorizationDeniedExceptionHandler(Exception ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( new ErrorResponse("Authorization Error", ex.getMessage()) );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsExceptionHandler(Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new ErrorResponse("Authentication Error", ex.getMessage()) );
    }
}