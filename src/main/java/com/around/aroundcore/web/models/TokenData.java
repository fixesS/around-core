package com.around.aroundcore.web.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenData {
    private String access_token;
    private String refresh_token;
}