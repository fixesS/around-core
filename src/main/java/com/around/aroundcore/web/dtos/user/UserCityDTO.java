package com.around.aroundcore.web.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "DTO for user cities info (team for each city)")
public class UserCityDTO {
    private Integer city_id;
    private Integer team_id;
    private Long captured_chunks;
}

