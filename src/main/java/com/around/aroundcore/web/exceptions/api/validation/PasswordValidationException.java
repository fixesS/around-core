package com.around.aroundcore.web.exceptions.api.validation;

import com.around.aroundcore.web.enums.ApiResponse;

public class PasswordValidationException extends ValidationException {
    public PasswordValidationException() {
        super(ApiResponse.AUTH_INCORRECT_PASSWORD_LENGTH);
    }
}
