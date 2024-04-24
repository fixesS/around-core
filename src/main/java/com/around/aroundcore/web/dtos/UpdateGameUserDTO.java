package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for update user info")
public class UpdateGameUserDTO {
    @Nullable
    @Size(min = 3, message = "-3005")
    private String username;
    @Nullable
    @Email(message = "-3002")
    @Size(min=1,message = "-3002")
    @Schema(description = "Email aka login", example = "email@example.com")
    private String email;
    @Nullable
    @Size(min=1,message = "-3008")
    @Schema(example = "Yekaterinburg")
    private String city;
}
