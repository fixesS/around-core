package com.around.aroundcore.web.exceptions.api.validation;

import com.around.aroundcore.web.enums.ApiResponse;

public class UsernameValidationException extends ValidationException {
    public UsernameValidationException() {
        super(ApiResponse.AUTH_INCORRECT_USERNAME_FORMAT);
    }
}
