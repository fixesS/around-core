package com.around.aroundcore.web.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO for user stat info")
public class UserStatDTO {
    @Nullable
    @Schema(description = "user id")
    private Integer id;
    private String username;
    private String avatar;
    @Schema(description = "number of captured chunks")
    private Integer number;
}
