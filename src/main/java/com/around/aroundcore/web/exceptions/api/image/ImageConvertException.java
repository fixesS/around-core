package com.around.aroundcore.web.exceptions.api.image;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class ImageConvertException extends ApiException {
    public ImageConvertException() {
        super(ApiResponse.UNKNOWN_ERROR);
    }
}
