package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class CityNullException extends EntityNullException{
    public CityNullException(){
        super("City is null", ApiResponse.CITY_DOES_NOT_EXIST);
    }
}
