package com.around.aroundcore.core.exceptions.api.image;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class ImageSaveException extends ApiException {
    public ImageSaveException(){
        super(ApiResponse.UNKNOWN_ERROR);
    }
}