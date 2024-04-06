package com.around.aroundcore.web.dto;

import com.around.aroundcore.web.enums.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Payload;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "DTO for login")
public class AuthDTO {
    @Email(message = "-3002")
    @NotBlank(message = "-3002")
    @Schema(description = "Email aka login", example = "email@example.com")
    private String email;
    @NotBlank(message = "-3003")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "-3003")
    @Schema(example = "Password1!")
    private String password;
}
