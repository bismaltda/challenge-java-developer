package br.com.neurotech.challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.neurotech.challenge.dtos.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFoundException(ClientNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                "Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ClientNotEligibleException.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFoundException(ClientNotEligibleException ex) {
        ErrorResponse response = new ErrorResponse(
                "Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }



}