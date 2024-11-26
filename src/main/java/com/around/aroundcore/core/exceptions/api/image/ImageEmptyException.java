package com.around.aroundcore.core.exceptions.api.image;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class ImageEmptyException extends ApiException {
    public ImageEmptyException() {
        super(ApiResponse.IMAGE_EMPTY);
    }
}
