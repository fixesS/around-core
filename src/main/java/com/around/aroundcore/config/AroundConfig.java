package com.around.aroundcore.config;

import com.around.aroundcore.web.gson.GsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AroundConfig {
    public static final String API_V1_LOGIN = "api/v1/auth/login";
    public static final String API_V1_REGISTRATION = "api/v1/auth/registration";
    @Bean
    public GsonParser getGsonParser(){
        return new GsonParser();
    }

}
