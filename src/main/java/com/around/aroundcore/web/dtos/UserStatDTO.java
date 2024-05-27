package com.around.aroundcore.web.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO for user stat info")
public class UserStatDTO {
    @Schema(description = "user id")
    @Nullable
    private Integer id;
    @Schema(description = "number of captured chunks")
    private Integer number;
}
