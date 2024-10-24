package com.around.aroundcore.web.dtos.oauth.vk;

import lombok.Data;

@Data
public class VkUserModel {
    private Integer user_id;
    private String first_name;
    private String last_name;
    private String avatar;
    private String email;
}

