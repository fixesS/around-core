package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Deprecated
@Data
@Schema(description = "DTO for ok response")
public class ApiOk<E> {
    private Integer status;
    private String message;
    private E data;

}