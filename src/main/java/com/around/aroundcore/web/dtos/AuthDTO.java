package com.around.aroundcore.web.dtos;

import com.around.aroundcore.config.AroundConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "DTO for login")
public class AuthDTO {
    @Pattern(regexp = AroundConfig.EMAIL_REGEX ,message = "-3002")
    @NotBlank(message = "-3002")
    @Schema(description = "Email aka login", example = "email@example.com")
    private String email;
    @NotBlank(message = "-3003")
    @Pattern(regexp = AroundConfig.PASSWORD_REGEX, message = "-3003")
    @Schema(example = "Password1!")
    private String password;
}
