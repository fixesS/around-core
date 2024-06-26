package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for user info")
public class GameUserDTO {

    private Integer id;
    private String username;
    private String avatar;
    private String email;
    private String city;
    @Schema(description = "User team id")
    private Integer team_id;
    private Integer level;
    private Integer coins;
    @Schema(description = "Is user email verified")
    private Boolean verified;
    private Integer captured_chunks;
}

