package com.around.aroundcore.config;

import com.around.aroundcore.web.mappers.StringGameChunkDTOMapper;
import com.around.aroundcore.web.services.H3ChunkService;
import com.uber.h3core.H3Core;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class AroundConfig {
    public static final String API = "api";
    public static final String API_V1 = API+"/v1";
    public static final String API_V1_AUTH = API_V1+"/auth";
    public static final String API_V1_LOGIN = API_V1_AUTH+"/login";
    public static final String API_V1_REGISTRATION = API_V1_AUTH+"/registration";
    public static final String API_V1_RECOVERY = API_V1_AUTH+"/recovery";
    public static final String API_V1_USER = API_V1+"/user";
    public static final String API_V1_CHUNKS = API_V1+"/chunks";
    public static final String API_V1_STATISTIC = API_V1+"/stat";

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
    @Bean
    public H3Core h3Core() throws IOException {
        return H3Core.newInstance();
    }
    @Bean
    public StringGameChunkDTOMapper stringGameChunkDTOMapper(){
        return new StringGameChunkDTOMapper();
    }
    @Bean
    public H3ChunkService h3ChunkService() throws IOException {
        return new H3ChunkService(h3Core(),stringGameChunkDTOMapper());
    }
}
