package com.around.aroundcore.config;

import com.around.aroundcore.web.gson.GsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AroundConfig {
    public static final String API = "api/";
    public static final String API_V1 = API+"v1/";

    public static final String API_V1_AUTH = API_V1+"auth/";
    public static final String API_V1_LOGIN = API_V1_AUTH+"login";
    public static final String API_V1_REGISTRATION = API_V1_AUTH+"registration";
    @Bean
    public GsonParser getGsonParser(){
        return new GsonParser();
    }

}
