package com.around.aroundcore.web.exceptions.api.image;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class ImageEmptyException extends ApiException {
    public ImageEmptyException() {
        super(ApiResponse.IMAGE_EMPTY);
    }
}
