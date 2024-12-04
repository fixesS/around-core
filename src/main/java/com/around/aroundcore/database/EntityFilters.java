package com.around.aroundcore.database;

import lombok.Getter;

@Getter
public enum EntityFilters {
    ACTIVE_ROUND("activeRoundFilter","active", Boolean.class);

    private final String name;
    private final String parameter;
    private final Class parameterType;
    EntityFilters(String name, String parameter, Class parameterType) {
        this.name = name;
        this.parameter = parameter;
        this.parameterType = parameterType;
    }

}
