package com.around.aroundcore.core.enums;

import lombok.Getter;

@Getter
public enum EventProvidersEnum {
    TIMEPAD(0);

    private final Integer id;

    EventProvidersEnum(Integer id){
        this.id = id;
    }
}
