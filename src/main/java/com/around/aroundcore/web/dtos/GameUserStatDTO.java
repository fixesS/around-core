package com.around.aroundcore.web.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "DTO for user stat info")
public class GameUserStatDTO {
    @Nullable
    @Schema(description = "user id")
    private Integer id;
    private List<RoundStatDTO> roundStat;
    private Integer level;
    private String username;
    private String avatar;
}
