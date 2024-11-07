package com.around.aroundcore.web.exceptions.api.image;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class ImageSaveException extends ApiException {
    public ImageSaveException(){
        super(ApiResponse.UNKNOWN_ERROR);
    }
}
