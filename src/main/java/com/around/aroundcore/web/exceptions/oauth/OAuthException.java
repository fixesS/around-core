package com.around.aroundcore.web.exceptions.oauth;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class OAuthException extends ApiException {
    private String message;
    public OAuthException(String message) {
        super(ApiResponse.AUTH_OAUTH2_ERROR);
        this.message = message;
    }
}
