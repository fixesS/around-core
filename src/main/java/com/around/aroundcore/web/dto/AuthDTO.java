package com.around.aroundcore.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO for login")
public class AuthDTO {
    @Email(message = "-3002")
    @NotBlank(message = "-3002")
    @NotNull(message = "-3002")
    @Schema(description = "Email aka login", example = "email@example.com")
    private String email;
    @NotNull(message = "-3003")
    @NotBlank(message = "-3003")
    private String password;
}
