package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.ApiException;
import com.around.aroundcore.web.exceptions.AuthHeaderException;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.dto.ApiError;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ExceptionHandler(AuthHeaderException.class)
    public ResponseEntity<String> handleUnAuthorizedException(AuthHeaderException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleUnAuthorizedException(JwtException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleApiException(ApiException exception) {
        ApiResponse response = exception.getResponse();
        ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
        String body = gsonParser.toJson(apiError);
        return new ResponseEntity<>(body, response.getStatus());
    }
}
