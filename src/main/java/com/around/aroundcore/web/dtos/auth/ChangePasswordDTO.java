package com.around.aroundcore.web.dtos.auth;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.web.exceptions.api.entity.GameUserPasswordSame;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "DTO for resetting password")
public class ChangePasswordDTO {
    @NotBlank(message = "-3003")
    @Pattern(regexp = AroundConfig.PASSWORD_REGEX, message = "-3003")
    @Schema(example = "Password1!")
    private String old_password;
    @NotBlank(message = "-3003")
    @Pattern(regexp = AroundConfig.PASSWORD_REGEX, message = "-3003")
    @Schema(example = "Password1!")
    private String new_password;

    public void CheckIsPasswordsDifferent() throws GameUserPasswordSame{
        if(old_password.equals(new_password)){
            throw new GameUserPasswordSame();
        }
    }
}
