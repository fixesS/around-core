package com.around.aroundcore.web.dtos.oauth.google;

import lombok.Data;

@Data
public class GooglePerson {
    private String given_name;
    private String picture;
    private String id;
    private String name;
}
