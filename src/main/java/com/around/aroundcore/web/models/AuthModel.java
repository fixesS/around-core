package com.around.aroundcore.web.models;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class AuthModel {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
