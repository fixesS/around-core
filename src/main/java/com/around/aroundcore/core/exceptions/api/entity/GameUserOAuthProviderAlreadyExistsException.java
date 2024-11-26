package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class GameUserOAuthProviderAlreadyExistsException extends ApiException {
    public GameUserOAuthProviderAlreadyExistsException() {
        super(ApiResponse.USER_ALREADY_HAVE_OAUTH_ACCOUNT);
    }
}
