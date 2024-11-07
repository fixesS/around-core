package com.around.aroundcore.web.exceptions.image;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.entity.EntityNullException;

public class ImageNullException extends EntityNullException {
    public ImageNullException() {
        super("Image is null", ApiResponse.UNKNOWN_ERROR);
    }
}
