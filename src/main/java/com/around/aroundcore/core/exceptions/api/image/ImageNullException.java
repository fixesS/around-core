package com.around.aroundcore.core.exceptions.api.image;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.entity.EntityNullException;

public class ImageNullException extends EntityNullException {
    public ImageNullException() {
        super("Image is null", ApiResponse.UNKNOWN_ERROR);
    }
}
