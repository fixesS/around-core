package com.around.aroundcore.web.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for user info")
public class GameUserDTO {

    @Getter
    private Integer id;
    private String username;
    private String avatar;
    private String email;
    private List<UserCityDTO> cities;
    @Schema(description = "User team id")
    private Integer team_id;
    private Integer level;
    private Integer coins;
    @Schema(description = "Is user email verified")
    private Boolean verified;
    private List<GameUserOAuthProvider> providers;
}

