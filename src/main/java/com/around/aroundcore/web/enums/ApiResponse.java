package com.around.aroundcore.web.enums;

import com.around.aroundcore.web.models.ApiError;
import com.around.aroundcore.web.models.ApiOk;
import lombok.Getter;
import org.springframework.http.HttpStatus;


public enum ApiResponse {
    OK(200, HttpStatus.OK),
    UNKNOWN_ERROR(-1000, "Unknown error.",HttpStatus.BAD_REQUEST),
    USER_DOES_NOT_EXIST(-2001,"User does not exist", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXIST(-2002,"User does not exist", HttpStatus.BAD_REQUEST),
    LOG_INCORRECT_PASSWORD_OR_LOGIN(-3001,"Incorrect password or login.", HttpStatus.BAD_REQUEST),
    REG_INCORRECT_EMAIL_FORMAT(-3002,"Incorrect format of email.", HttpStatus.BAD_REQUEST),
    REG_INCORRECT_PASSWORD_FORMAT(-3003,"Password length must be between 5 and 20 symbols.", HttpStatus.BAD_REQUEST),
    REG_INCORRECT_USERNAME_FORMAT(-3004,"Username cannot be empty.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(-4001, "Token is invalid.",HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_DELETED(-4002,"Refresh token you use have been deleted(New one was created).", HttpStatus.BAD_REQUEST),
    SESSION_DOES_NOT_EXIST(-5001,"Session does not exist", HttpStatus.BAD_REQUEST),
    SESSION_EXPIRED(-5002,"Session is expired",HttpStatus.BAD_REQUEST);

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


