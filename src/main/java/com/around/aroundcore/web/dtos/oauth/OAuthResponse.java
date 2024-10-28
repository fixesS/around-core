package com.around.aroundcore.web.dtos.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
@Data
public class OAuthResponse {
    private String user_id;
    private String first_name;
    private String last_name;
    private String avatar;
    private String email;
}
