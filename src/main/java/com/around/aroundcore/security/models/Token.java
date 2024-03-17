package com.around.aroundcore.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Token {
    private String token;
    private Date expiresIn;
    private Date created;

}
