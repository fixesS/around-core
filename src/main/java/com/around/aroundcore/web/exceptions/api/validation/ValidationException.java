package com.around.aroundcore.web.exceptions.api.validation;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class ValidationException extends ApiException {
    public ValidationException(ApiResponse response) {
        super(response);
    }
}
