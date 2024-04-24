package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO for registration")
public class RegistrationDTO {
    @NotBlank(message = "-3004")
    @Size(min = 3, message = "-3005")
    private String username;
    @Email(message = "-3002")
    @NotBlank(message = "-3002")
    @Schema(description = "Email aka login", example = "email@example.com")
    private String email;
    @NotBlank(message = "-3003")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "-3003")
    @Schema(example = "Password1!")
    private String password;

    @NotNull(message = "-3006")
    @Min(value = 1, message = "-3007")
    @Schema(example = "1")
    private Integer team_id;
    @NotBlank(message = "-3008")
    @Schema(example = "Yekaterinburg")
    private String city;

}
