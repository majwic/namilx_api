package com.majwic.exception;

import com.majwic.util.JsonBuilder;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Schema(description = "Handles any errors and returns appropriate ErrorResponse")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Hidden
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    @Hidden
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponse("UNAUTHORIZED", ex.getMessage()));
    }

    @Hidden
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleConflictException(ConflictException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponse("CONFLICT", ex.getMessage()));
    }

    @Hidden
    @ExceptionHandler(FormatException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<String> handleFormatException(FormatException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponse("NOT_ACCEPTABLE", ex.getMessage()));
    }
    
    private static String buildErrorResponse(String error, String message) {
        return new JsonBuilder()
            .add("error", error)
            .add("message", message)
            .add("timestamp", LocalDateTime.now().toString())
            .build();
    }
}
