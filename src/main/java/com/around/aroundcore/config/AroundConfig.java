package com.around.aroundcore.config;

import com.around.aroundcore.web.enums.CoordsAPIType;
import com.around.aroundcore.web.mappers.StringGameChunkDTOMapper;
import com.around.aroundcore.web.services.H3ChunkService;
import com.around.aroundcore.web.services.apis.coords.CoordsAPI;
import com.around.aroundcore.web.services.apis.coords.DadataAPIService;
import com.around.aroundcore.web.services.apis.coords.GeotreeAPIService;
import com.kuliginstepan.dadata.client.DadataClient;
import com.uber.h3core.H3Core;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class AroundConfig {
    public static final String API = "api";
    public static final String API_V1 = API+"/v1";
    public static final String API_V1_AUTH = API_V1+"/auth";
    public static final String API_V1_LOGIN = API_V1_AUTH+"/login";
    public static final String API_V1_REGISTRATION = API_V1_AUTH+"/registration";
    public static final String API_V1_REFRESH= API_V1_AUTH+"/refresh";
    public static final String API_V1_RECOVERY = API_V1_AUTH+"/recovery";
    public static final String API_V1_USER = API_V1+"/user";
    public static final String API_V1_TEAM = API_V1+"/team";
    public static final String API_V1_EVENTS = API_V1+"/map-events";
    public static final String API_V1_CHUNKS = API_V1+"/chunks";
    public static final String API_V1_STATISTIC = API_V1+"/stat";
    public static final String API_V1_SKILLS = API_V1+"/skills";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#%^&*()_+\\-=:;” '{}<>?\\|`~,.])[a-zA-Z\\d!@#%^&*()_+\\-=:;” '{}<>?\\|`~,.]{8,100}$";
    public static final String EMAIL_REGEX = "^[\\w-]+@([\\w-]+\\.)+[\\w-]+";
    public static final String USERNAME_REGEX = "[a-zA-Z0-9]+";
    @Value("${around.coordsapi}")
    private String coordsAPIType;
    @Value("${geotree.api.key}")
    private String geotreeKey;
    @Value("${geotree.api.url}")
    private String geotreeMainUrl;
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
    @Bean
    public RestTemplate restTemplate() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
    @Bean
    public CoordsAPI coordsAPI(@Autowired DadataClient dadataClient){
        CoordsAPIType type = CoordsAPIType.valueOf(coordsAPIType);
        switch (type){
            case DADATA -> {
                return new DadataAPIService(CoordsAPIType.DADATA,dadataClient);
            }
            case GEOTREE -> {
                return new GeotreeAPIService(CoordsAPIType.GEOTREE, restTemplate(),geotreeKey,geotreeMainUrl);
            }
            default -> throw new IllegalStateException("Unexpected CoordsType: " + type);
        }
    }
}
