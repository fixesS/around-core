package com.around.aroundcore.web.exceptions.image;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class ImageTypeException extends ApiException {
    public ImageTypeException() {
        super(ApiResponse.IMAGE_TYPE_ERROR);
    }
}
