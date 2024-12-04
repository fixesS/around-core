package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class MapEventNullException extends EntityNullException{
    public MapEventNullException() {
        super("Event is null",ApiResponse.EVENT_DOES_NOT_EXIST);
    }
}
