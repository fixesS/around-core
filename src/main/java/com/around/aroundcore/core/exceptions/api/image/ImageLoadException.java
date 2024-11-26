package com.around.aroundcore.core.exceptions.api.image;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class ImageLoadException extends ApiException {
    public ImageLoadException(){
        super(ApiResponse.IMAGE_LOAD_ERROR);
    }
}
