package com.around.aroundcore.web.dtos;

import com.around.aroundcore.annotations.NotRead;
import com.around.aroundcore.config.AroundConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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
    @Size(min=1,message = "-3005")
    @Pattern(regexp = AroundConfig.USERNAME_REGEX, message = "-3005")
    private String username;
    @Nullable
    @NotRead
    @Min(value = 1, message = "-3008")
    @Schema(example = "Yekaterinburg")
    private Integer city_id;
    @Nullable
    @Min(value = 1, message = "-3007")
    @Schema(example = "1")
    @NotRead
    private Integer team_id;
}
