package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class GameUserOAuthProviderAlreadyExistsException extends ApiException {
    public GameUserOAuthProviderAlreadyExistsException() {
        super(ApiResponse.USER_ALREADY_HAVE_OAUTH_ACCOUNT);
    }
}
