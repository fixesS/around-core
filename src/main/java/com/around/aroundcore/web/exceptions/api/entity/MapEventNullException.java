package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class MapEventNullException extends EntityNullException{
    public MapEventNullException() {
        super("Event is null",ApiResponse.EVENT_DOES_NOT_EXIST);
    }
}
