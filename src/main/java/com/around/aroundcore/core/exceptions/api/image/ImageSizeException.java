package com.around.aroundcore.core.exceptions.api.image;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class ImageSizeException extends ApiException {
    public ImageSizeException() {
        super(ApiResponse.IMAGE_SIZE_TOO_BIG);
    }
}
