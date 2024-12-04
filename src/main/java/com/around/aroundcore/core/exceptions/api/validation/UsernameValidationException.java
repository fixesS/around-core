package com.around.aroundcore.core.exceptions.api.validation;

import com.around.aroundcore.core.enums.ApiResponse;

public class UsernameValidationException extends ValidationException {
    public UsernameValidationException() {
        super(ApiResponse.AUTH_INCORRECT_USERNAME_FORMAT);
    }
}
