package com.around.aroundcore.core.enums;

import lombok.Getter;

@Getter
public enum Skills {
    WIDTH(1);

    private final Integer id;

    Skills(Integer id){
        this.id = id;
    }
}
