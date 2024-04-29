package com.around.aroundcore.web.enums;

import lombok.Getter;

@Getter
public enum CityEnum {
    EKB("Екатеринбург");

    private final String name;

    CityEnum(String name){
        this.name = name;
    }
}
