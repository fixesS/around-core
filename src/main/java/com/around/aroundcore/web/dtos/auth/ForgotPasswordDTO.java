package com.around.aroundcore.web.dtos.auth;

import com.around.aroundcore.config.AroundConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "DTO for starting recovery process")
public class ForgotPasswordDTO {
    @Pattern(regexp = AroundConfig.EMAIL_REGEX ,message = "-3002")
    @NotBlank(message = "-3002")
    @Schema(description = "Email aka login", example = "email@example.com")
    private String email;
}
