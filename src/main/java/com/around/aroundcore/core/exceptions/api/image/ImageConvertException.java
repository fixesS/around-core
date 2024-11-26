package com.around.aroundcore.core.exceptions.api.image;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class ImageConvertException extends ApiException {
    public ImageConvertException() {
        super(ApiResponse.UNKNOWN_ERROR);
    }
}
