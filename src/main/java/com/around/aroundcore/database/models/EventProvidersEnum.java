package com.around.aroundcore.database.models;

import lombok.Getter;

@Getter
public enum EventProvidersEnum {
    TIMEPAD(0);

    private final Integer id;

    EventProvidersEnum(Integer id){
        this.id = id;
    }
}
