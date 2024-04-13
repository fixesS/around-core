package com.around.aroundcore.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "DTO for starting recovery process")
public class ForgotPasswordDTO {
    @Email(message = "-3002")
    @NotBlank(message = "-3002")
    @Schema(description = "Email aka login", example = "email@example.com")
    private String email;
}
