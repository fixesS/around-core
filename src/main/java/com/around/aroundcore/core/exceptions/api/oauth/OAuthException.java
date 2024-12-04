package com.around.aroundcore.core.exceptions.api.oauth;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class OAuthException extends ApiException {
    public OAuthException(String message) {
        super(ApiResponse.AUTH_OAUTH2_ERROR, message);
    }
}
