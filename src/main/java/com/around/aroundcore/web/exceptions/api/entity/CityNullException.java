package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class CityNullException extends EntityNullException{
    public CityNullException(){
        super("City is null", ApiResponse.CITY_DOES_NOT_EXIST);
    }
}
