package com.around.aroundcore.web.enums;

import com.around.aroundcore.web.dto.ApiError;
import com.around.aroundcore.web.dto.ApiOk;
import lombok.Getter;
import org.springframework.http.HttpStatus;


public enum ApiResponse {
    OK(200, HttpStatus.OK),
    UNKNOWN_ERROR(-1000, "Unknown error."),
    USER_DOES_NOT_EXIST(-2001,"User does not exist."),
    USER_ALREADY_EXIST(-2002,"User already exist."),
    USER_NOT_UNIQUE_EMAIL(-2003,"User email is not unique."),
    USER_NOT_UNIQUE_USERNAME(-2004,"User username is not unique."),
    LOG_INCORRECT_PASSWORD_OR_LOGIN(-3001,"Incorrect password or login."),
    AUTH_INCORRECT_EMAIL_FORMAT(-3002,"Incorrect format of email."),
    AUTH_INCORRECT_PASSWORD_LENGTH(-3003,"Password length must be between 8 and 20 symbols,at least one uppercase letter, one lowercase letter, one number and one special character(@,$,!,%,*,?,&)"),
    AUTH_INCORRECT_USERNAME_FORMAT(-3004,"Username cannot be empty."),
    AUTH_INCORRECT_USERNAME_SIZE(-3005,"Username length must be more than 2."),
    INVALID_TOKEN(-4001, "Token is invalid."),
    REFRESH_TOKEN_ALREADY_USED(-4002,"Refresh token already has been used."),
    SESSION_DOES_NOT_EXIST(-5001,"Session does not exist."),
    SESSION_EXPIRED(-5002,"Session is expired."),
    CHUNK_DOES_NOT_EXIST(-6001,"Chunk you trying get does not exist or has not been captured by any user.");

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
    ApiResponse(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
    ApiResponse(Integer statusCode, String message, HttpStatus status) {
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public static ApiResponse findByStatusCode(int statusCode){
        for (ApiResponse response: ApiResponse.values()){
            if(response.statusCode == statusCode){
                return response;
            }
        }
        return ApiResponse.UNKNOWN_ERROR;
    }
    public static <T> ApiOk<T> getApiOk(Integer statusCode, String message, T data){
        ApiOk<T> apiOk = new ApiOk<>();
        apiOk.setStatus(statusCode);
        apiOk.setMessage(message);
        apiOk.setData(data);

        return apiOk;
    }
    public static ApiError getApiError(Integer statusCode, String message){
        ApiError apiError = new ApiError();
        apiError.setStatus(statusCode);
        apiError.setMessage(message);

        return apiError;
    }
}


