package com.dhiren.cloud.exceptions;

import com.dhiren.cloud.exceptions.custom.LibraryEventNotFoundException;
import com.dhiren.cloud.exceptions.custom.ValidationBusinessException;
import com.dhiren.cloud.model.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        CustomResponse response = new CustomResponse(errors, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationBusinessException.class)
    public ResponseEntity<CustomResponse> handleValidationExceptions(ValidationBusinessException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errors",ex.getMessage());
        CustomResponse response = new CustomResponse(errorMap, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibraryEventNotFoundException.class)
    public ResponseEntity<CustomResponse> handleLibraryEventNotFoundException(LibraryEventNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errors",ex.getMessage());
        CustomResponse response = new CustomResponse(errorMap, Instant.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
