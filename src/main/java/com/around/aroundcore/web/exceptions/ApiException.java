package com.around.aroundcore.web.exceptions;

import com.around.aroundcore.web.enums.ApiResponse;
import lombok.Getter;

public class ApiException extends RuntimeException{
    public ApiException() {
        super();
        this.response = ApiResponse.UNKNOWN_ERROR;

    }

    public ApiException(ApiResponse response) {
        super();
        this.response = response;
    }
    @Getter
    private final ApiResponse response;
}
