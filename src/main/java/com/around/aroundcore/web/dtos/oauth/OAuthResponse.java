package com.around.aroundcore.web.dtos.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class OAuthResponse {
    private Long user_id;
    private String first_name;
    private String last_name;
    private String avatar;
    private String email;
}
