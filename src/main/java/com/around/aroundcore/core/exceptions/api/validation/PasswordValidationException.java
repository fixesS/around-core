package com.around.aroundcore.core.exceptions.api.validation;

import com.around.aroundcore.core.enums.ApiResponse;

public class PasswordValidationException extends ValidationException {
    public PasswordValidationException() {
        super(ApiResponse.AUTH_INCORRECT_PASSWORD_LENGTH);
    }
}
