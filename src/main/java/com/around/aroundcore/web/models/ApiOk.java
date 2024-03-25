package com.around.aroundcore.web.models;

import lombok.Data;

@Data
public class ApiOk<E> {
    private Integer status;
    private String message;
    private E data;
}