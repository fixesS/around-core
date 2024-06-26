package com.around.aroundcore.web.enums;

import com.around.aroundcore.web.dtos.ApiError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ApiResponse {
    OK(200, HttpStatus.OK),
    UNKNOWN_ERROR(-1000, "Unknown error."),
    USER_DOES_NOT_EXIST(-2001,"User does not exist."),
    USER_ALREADY_EXIST(-2002,"User already exist."),
    USER_NOT_UNIQUE_EMAIL(-2003,"User email is not unique."),
    USER_NOT_UNIQUE_USERNAME(-2004,"User username is not unique."),
    USER_HAS_NO_TEAM(-2005,"User has no team."),
    USER_IS_NOT_VERIFIED(-2006,"User email is not verified."),
    USER_NEW_PASSWORD_THE_SAME(-2007,"User password is the same as previous."),
    USER_CANNOT_BE_FRIEND_TO_HIMSELF(-2008,"User cannot be friend to himself."),
    USER_FOLLOW_ALREADY_EXIST(-2008,"You have already followed this user."),
    USER_NOT_ENOUGH_COINS(-2009,"You do not have enough coins for transaction."),
    LOG_INCORRECT_PASSWORD_OR_LOGIN(-3001,"Incorrect password or login."),
    AUTH_INCORRECT_EMAIL_FORMAT(-3002,"Incorrect format of email."),
    AUTH_INCORRECT_PASSWORD_LENGTH(-3003,"Password length must be at least 8 symbols, one uppercase letter, one lowercase letter, one number and one special character(!,@,#,%,^,&,*,(,),_,+,-,=,:,;,”,<space>,',{,},<,>,?,|,`,~,<comma>,."),
    AUTH_INCORRECT_USERNAME_FORMAT(-3004,"Username cannot be empty."),
    AUTH_INCORRECT_USERNAME_SIZE(-3005,"Username length must be more than 2."),
    AUTH_INCORRECT_TEAM_ID_NULL(-3006,"Team id cannot be empty."),
    AUTH_INCORRECT_TEAM_ID_FORMAT(-3007,"Team id cannot be less than 1."),
    AUTH_INCORRECT_CITY_FORMAT(-3008,"City cannot be empty."),
    AUTH_INCORRECT_TYPE_OF_FIELD(-3009,"Incorrect type of field."),
    AUTH_INCORRECT_AVATAR(-3010,"Avatar must be not empty."),
    INVALID_TOKEN(-4001, "Token is invalid."),
    REFRESH_TOKEN_ALREADY_USED(-4002,"Refresh token already has been used."),
    VERIFIED_TOKEN_EXPIRED(-4003,"VerifiedToken is invalid"),
    RECOVERY_TOKEN_EXPIRED(-4004,"RecoveryToken is invalid"),
    SESSION_DOES_NOT_EXIST(-5001,"Session does not exist."),
    SESSION_EXPIRED(-5002,"Session is expired."),
    CHUNK_DOES_NOT_EXIST(-6001,"Chunk you trying get does not exist or has not been captured by any user."),
    TEAM_DOES_NOT_EXIST(-7001,"Team does not exist."),
    SKILL_DOES_NOT_EXIST(-8001,"Skill does not exist."),
    SKILL_LEVEL_UNREACHABLE(-8002,"Skill level you trying to get more than max level for skill."),
    SKILL_LEVEL_ALREADY_MAX(-8003,"Your level of skill is already max."),
    LEVELS_MUST_BE_MORE_THAN_ZERO(-8004,"Levels you tying to add(buy) must be more than 0.");

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
    public static ApiError getApiError(Integer statusCode, String message){
        ApiError apiError = new ApiError();
        apiError.setStatus(statusCode);
        apiError.setMessage(message);

        return apiError;
    }
    @Override
    public String toString(){
        return this.statusCode.toString();
    }
}


