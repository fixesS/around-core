package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.models.ApiError;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@AllArgsConstructor
public class RestExceptionHandler {
    GsonParser gsonParser;
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        ObjectError lastErr = errors.get(errors.size()-1);

        String message = ((FieldError) lastErr).getDefaultMessage();
        int statusCode = Integer.parseInt(message);

        ApiResponse response = ApiResponse.findByStatusCode(statusCode);
        ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
        String body = gsonParser.toJson(apiError);
        return new ResponseEntity<>(body, response.getStatus());
    }
}
