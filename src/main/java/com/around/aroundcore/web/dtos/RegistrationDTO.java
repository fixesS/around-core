package com.around.aroundcore.web.dtos;

import com.around.aroundcore.config.AroundConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO for registration")
public class RegistrationDTO {
    @NotBlank(message = "-3004")
    @Pattern(regexp = AroundConfig.USERNAME_REGEX, message = "-3005")
    private String username;
    @Nullable
    @Size(min=1,message = "-3010")
    @Schema(example = "1")
    private String avatar;
    @Pattern(regexp = AroundConfig.EMAIL_REGEX ,message = "-3002")
    @NotBlank(message = "-3002")
    @Schema(description = "Email aka login", example = "email@example.com")
    private String email;
    @NotBlank(message = "-3003")
    @Pattern(regexp = AroundConfig.PASSWORD_REGEX, message = "-3003")
    @Schema(example = "Password1!")
    private String password;
    @Nullable
    @Min(value = 1, message = "-3007")
    @Schema(example = "1")
    private Integer team_id;
    @Nullable
    @Size(min=1,message = "-3008")
    @Schema(example = "Yekaterinburg")
    private String city;

}
