package com.around.aroundcore.config;

import com.around.aroundcore.security.filters.UpgradeHttpToWebSocketHandshakeHandler;
import com.around.aroundcore.security.filters.HttpHandshakeInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    public WebSocketConfig(AuthConfig authConfig){
        this.authConfig = authConfig;
    }
    private final AuthConfig authConfig;
    @Value("${spring.rabbitmq.port}")
    Integer port;
    @Value("${spring.rabbitmq.host}")
    String host;

    @Value("${spring.rabbitmq.username}")
    String username;

    @Value("${spring.rabbitmq.password}")
    String password;

    public static final String TOPIC_DESTINATION_PREFIX = "/topic";
    public static final String QUEUE_DESTINATION_PREFIX = "/queue";
    public static final String EXCHANGE_DESTINATION_PREFIX = "/exchange";
    public static final String PRIVATE_DESTINATION_PREFIX = "/private.message";
    public static final String APP_DESTINATION_PREFIX = "/app";
    public static final String REGISTRY = "/ws";

    public static final String QUEUE_ERROR_FOR_USER = "/exchange/private.message/error";
    public static final String QUEUE_LOCATIONS_FOR_USER = "/exchange/private.message/locations";


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(REGISTRY)
                .addInterceptors(handshakeInterceptor())
                .setHandshakeHandler(handshakeHandler())
                .setAllowedOrigins("*");
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config
                .setApplicationDestinationPrefixes(APP_DESTINATION_PREFIX)
                .enableStompBrokerRelay(TOPIC_DESTINATION_PREFIX,QUEUE_DESTINATION_PREFIX, PRIVATE_DESTINATION_PREFIX, EXCHANGE_DESTINATION_PREFIX)
                .setRelayPort(port)
                .setRelayHost(host)
                .setClientLogin(username)
                .setClientPasscode(password)
                .setSystemLogin(username)
                .setSystemPasscode(password);
    }
    @Bean
    public UpgradeHttpToWebSocketHandshakeHandler handshakeHandler(){
        return new UpgradeHttpToWebSocketHandshakeHandler(authConfig.sessionService);
    }
    @Bean
    public HttpHandshakeInterceptor handshakeInterceptor(){
        return new HttpHandshakeInterceptor(authConfig.sessionService, new ObjectMapper());
    }
}
