package com.around.aroundcore.core.exceptions.api;

import com.around.aroundcore.core.enums.ApiResponse;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    public ApiException() {
        super();
        this.response = ApiResponse.UNKNOWN_ERROR;

    }

    public ApiException(ApiResponse response) {
        super();
        this.response = response;
    }
    public ApiException(ApiResponse response, String message) {
        super(message);
        this.response = response;
    }
    private final ApiResponse response;

}
