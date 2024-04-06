package com.around.aroundcore.config;

import com.around.aroundcore.web.gson.GsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class AroundConfig {
    public static final String API = "api/";
    public static final String API_V1 = API+"v1/";

    public static final String API_V1_AUTH = API_V1+"auth/";
    public static final String API_V1_LOGIN = API_V1_AUTH+"login";
    public static final String API_V1_REGISTRATION = API_V1_AUTH+"registration";
    public static final String API_V1_USER = API_V1+"user";
    public static final String API_V1_CHUNKS = API_V1+"chunks";
    @Bean
    public GsonParser getGsonParser(){
        return new GsonParser();
    }
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(2);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

}
