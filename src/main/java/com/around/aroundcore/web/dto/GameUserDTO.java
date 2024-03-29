package com.around.aroundcore.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameUserDTO {
    private String username;
    private String email;
    private String city;
    private Integer team_id;
    private Integer level;
    private Integer coins;
}
