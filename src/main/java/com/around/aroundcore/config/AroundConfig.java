package com.around.aroundcore.config;

import com.around.aroundcore.web.services.ChunkQueueService;
import com.around.aroundcore.web.tasks.ChunkEventTask;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@AllArgsConstructor
public class AroundConfig {
    public static final String API = "api/";
    public static final String API_V1 = API+"v1/";

    public static final String API_V1_AUTH = API_V1+"auth/";
    public static final String API_V1_LOGIN = API_V1_AUTH+"login";
    public static final String API_V1_REGISTRATION = API_V1_AUTH+"registration";
    public static final String API_V1_USER = API_V1+"user";
    public static final String API_V1_CHUNKS = API_V1+"chunks";
    public static final String API_V1_STATISTIC = API_V1+"stat";

    private ChunkQueueService chunkQueueService;
    private SimpMessagingTemplate messagingTemplate;
    @Bean
    public ChunkEventTask chunkEventTask(){
        return new ChunkEventTask(chunkQueueService, messagingTemplate);
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
