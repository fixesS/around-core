package com.around.aroundcore.web.enums;

import lombok.Getter;

@Getter
public enum CityEnum {
    Yekaterinburg("Екатеринбург",1);

    private final String name;
    private final Integer id;

    CityEnum(String name, Integer id) {
        this.name = name;
        this.id = id;
    }
}
