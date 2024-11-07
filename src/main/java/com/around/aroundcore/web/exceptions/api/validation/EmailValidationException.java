package com.around.aroundcore.web.exceptions.api.validation;

import com.around.aroundcore.web.enums.ApiResponse;

public class EmailValidationException extends ValidationException {
    public EmailValidationException() {
        super(ApiResponse.AUTH_INCORRECT_EMAIL_FORMAT);
    }
}
