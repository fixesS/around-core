package com.around.aroundcore.config;

import com.around.aroundcore.core.statemachine.GameStates;
import com.around.aroundcore.core.statemachine.RoundEvents;
import com.around.aroundcore.core.enums.CoordsAPIType;
import com.around.aroundcore.web.mappers.chunk.StringGameChunkDTOMapper;
import com.around.aroundcore.core.services.H3ChunkService;
import com.around.aroundcore.core.services.apis.coords.CoordsAPI;
import com.around.aroundcore.core.services.apis.coords.DadataAPIService;
import com.around.aroundcore.core.services.apis.coords.GeotreeAPIService;
import com.kuliginstepan.dadata.client.DadataClient;
import com.uber.h3core.H3Core;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@EnableCaching
@EnableAsync
@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class AroundConfig {
    public static final String API = "api";
    public static final String API_V1 = API+"/v1";
    public static final String API_V1_AUTH = API_V1+"/auth";
    public static final String API_V1_OAUTH = API_V1_AUTH+"/oauth2";
    public static final String API_V1_LOGIN = API_V1_AUTH+"/login";
    public static final String API_V1_REGISTRATION = API_V1_AUTH+"/registration";
    public static final String API_V1_REFRESH= API_V1_AUTH+"/refresh";
    public static final String API_V1_RECOVERY = API_V1_AUTH+"/recovery";
    public static final String API_V1_USER = API_V1+"/users";
    public static final String API_V1_TEAM = API_V1+"/teams";
    public static final String API_V1_ROUND = API_V1+"/rounds";
    public static final String API_V1_EVENTS = API_V1+"/map-events";
    public static final String API_V1_CHUNKS = API_V1+"/chunks";
    public static final String API_V1_STATISTIC = API_V1+"/stats";
    public static final String API_V1_SKILLS = API_V1+"/skills";
    public static final String API_V1_IMAGE = API_V1+"/images";
    public static final String API_V1_CITY = API_V1+"/cities";
    public static final String API_V1_ADMIN = API_V1+"/admin";
    public static final String URL_AVATAR = "/"+AroundConfig.API_V1_IMAGE+"/avatars/";
    public static final String URL_ICON = "/"+AroundConfig.API_V1_IMAGE+"/icons/";
    public static final String URL_IMAGE = "/"+AroundConfig.API_V1_IMAGE+"/";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#%^&*()_+\\-=:;” '{}<>?\\|`~,.])[a-zA-Z\\d!@#%^&*()_+\\-=:;” '{}<>?\\|`~,.]{8,100}$";
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,6}$";
    public static final String USERNAME_REGEX = "[a-zA-Z0-9.\\-]{2,40}";
    @Value("${around.coordsapi}")
    private String coordsAPIType;
    @Value("${geotree.api.key}")
    private String geotreeKey;
    @Value("${geotree.api.url}")
    private String geotreeMainUrl;
    @Value("${around.chunks.resolution}")
    private Integer chunksResolution;
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
        return new H3ChunkService(h3Core(),stringGameChunkDTOMapper(),chunksResolution);
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
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("defaultAvatar","currentRound", "verifiedAndActiveEventsByCity","checkRound","getRoundById","checkCity","findCityById");
    }
    @Bean
    public StateMachine<GameStates, RoundEvents> stateMachine(StateMachineFactory<GameStates, RoundEvents> factory){
        StateMachine<GameStates, RoundEvents> stateMachine =  factory.getStateMachine();
        stateMachine.startReactively().subscribe();
        return stateMachine;
    }
}
