package com.around.aroundcore.core.exceptions.api.image;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class ImageTypeException extends ApiException {
    public ImageTypeException() {
        super(ApiResponse.IMAGE_TYPE_ERROR);
    }
}
