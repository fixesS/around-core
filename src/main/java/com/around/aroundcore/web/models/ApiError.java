package com.around.aroundcore.web.models;

import lombok.Data;

@Data
public class ApiError {
    private Integer status;
    private String message;

}