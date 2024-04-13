package com.around.aroundcore.config;

import com.around.aroundcore.web.services.ChunkQueueService;
import com.around.aroundcore.web.tasks.CheckTokensTask;
import com.around.aroundcore.web.tasks.ChunkEventTask;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.parameters.P;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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

}
