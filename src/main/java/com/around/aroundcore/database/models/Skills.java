package com.around.aroundcore.database.models;

import lombok.Getter;

@Getter
public enum Skills {
    WIDTH(1);

    private final Integer id;

    Skills(Integer id){
        this.id = id;
    }
}
