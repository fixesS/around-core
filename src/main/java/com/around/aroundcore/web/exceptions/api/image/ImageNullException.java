package com.around.aroundcore.web.exceptions.api.image;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.entity.EntityNullException;

public class ImageNullException extends EntityNullException {
    public ImageNullException() {
        super("Image is null", ApiResponse.UNKNOWN_ERROR);
    }
}
