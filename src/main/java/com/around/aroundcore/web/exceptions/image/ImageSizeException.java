package com.around.aroundcore.web.exceptions.image;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class ImageSizeException extends ApiException {
    public ImageSizeException() {
        super(ApiResponse.IMAGE_SIZE_TOO_BIG);
    }
}
