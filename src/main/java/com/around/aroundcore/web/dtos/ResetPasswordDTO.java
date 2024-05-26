package com.around.aroundcore.web.dtos;

import com.around.aroundcore.config.AroundConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "DTO for resetting password")
public class ResetPasswordDTO {
    @NotBlank(message = "-3003")
    @Pattern(regexp = AroundConfig.PASSWORD_REGEX, message = "-3003")
    @Schema(example = "Password1!")
    private String password;
}
