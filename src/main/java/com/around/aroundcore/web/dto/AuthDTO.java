package com.around.aroundcore.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthDTO {
    @Email(message = "-3002")
    @NotBlank(message = "-3002")
    @NotNull(message = "-3002")
    private String email;
    @NotNull(message = "-3003")
    @NotBlank(message = "-3003")
    private String password;
}
