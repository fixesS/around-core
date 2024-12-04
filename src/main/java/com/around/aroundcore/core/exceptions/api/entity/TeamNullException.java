package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class TeamNullException extends EntityNullException{
    public TeamNullException(){
        super("Team is null", ApiResponse.TEAM_DOES_NOT_EXIST);
    }

    public TeamNullException(String message){
        super(message);
    }
}
