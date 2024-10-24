package com.around.aroundcore.web.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO for tokens")
public class TokenData {
    @Schema(description = "Token for data access")
    private String access_token;

    @Schema(description = "Token for refresh access_token")
    private String refresh_token;
}
