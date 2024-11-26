package com.around.aroundcore.core.exceptions.api.validation;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class ValidationException extends ApiException {
    public ValidationException(ApiResponse response) {
        super(response);
    }
}
