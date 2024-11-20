package com.around.aroundcore.web.dtos.auth;

import com.around.aroundcore.database.models.oauth.OAuthProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "DTO for login")
public class OAuthDTO {
    @Schema(description = "Provider", example = "GOOGLE or VK")
    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;
    @Schema(example = "Password1!")
    private String access_token;
}
