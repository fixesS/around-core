package com.around.aroundcore.config;

import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.interceptors.AuthorizationSocketInterceptor;
import com.around.aroundcore.web.interceptors.HttpHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private JwtConfig jwtConfig;
    @Value("${spring.rabbitmq.port}")
    Integer port;
    @Value("${spring.rabbitmq.host}")
    String host;

    @Value("${spring.rabbitmq.username}")
    String username;

    @Value("${spring.rabbitmq.password}")
    String password;

    public static final String TOPIC_DESTINATION_PREFIX = "/topic/";
    public static final String REGISTRY = "/ws";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(REGISTRY).setAllowedOrigins("*");
        //registry.addEndpoint(REGISTRY);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        //registration.interceptors(authorizationInterceptor());
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableStompBrokerRelay(TOPIC_DESTINATION_PREFIX)
                .setRelayPort(port)
                .setRelayHost(host)
                .setClientLogin(username)
                .setClientPasscode(password)
                .setSystemLogin(username)
                .setSystemPasscode(password);
    }

    @Bean
    public AuthorizationSocketInterceptor authorizationInterceptor(){
        return new AuthorizationSocketInterceptor(sessionService, jwtConfig.jwtService());
    }
    @Bean
    public HttpHandshakeInterceptor httpHandshakeInterceptor(){
        return new HttpHandshakeInterceptor(jwtConfig.jwtService());
    }
}
