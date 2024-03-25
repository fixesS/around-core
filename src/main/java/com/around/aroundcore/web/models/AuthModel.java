package com.around.aroundcore.web.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthModel {
    @Email(message = "-3002")
    @NotBlank(message = "-3002")
    @NotNull(message = "-3002")
    private String email;
    @NotNull(message = "-3003")
    @NotBlank(message = "-3003")
    private String password;
}
