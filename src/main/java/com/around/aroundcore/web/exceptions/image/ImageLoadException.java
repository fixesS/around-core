package com.around.aroundcore.web.exceptions.image;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class ImageLoadException extends ApiException {
    public ImageLoadException(){
        super(ApiResponse.IMAGE_LOAD_ERROR);
    }
}
