package com.around.aroundcore.web.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;


public enum ApiResponse {
    OK(200, HttpStatus.OK),
    USER_DOES_NOT_EXIST(-1000,"User does not exist", HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR(-2000, "Unknown error.",HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD_OR_LOGIN(-3000,"Incorrect password or login.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(-5000, "Token is invalid.",HttpStatus.BAD_REQUEST),
    SESSION_DOES_NOT_EXIST(-6000,"Session does not exist", HttpStatus.BAD_REQUEST),
    SESSION_EXPIRED(-7000,"Session is expired",HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_DELETED(-8000,"Refresh token you use have been deleted(New one was created).", HttpStatus.BAD_REQUEST);

    @Getter
    private Integer statusCode;
    @Getter
    private String message;
    @Getter
    private HttpStatus status;
    ApiResponse() {

    }
    ApiResponse(Integer statusCode, HttpStatus status) {
        this.statusCode = statusCode;
        this.status = status;
    }
    ApiResponse(Integer statusCode, String message, HttpStatus status) {
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


