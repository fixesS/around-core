package com.around.aroundcore.config;

import com.around.aroundcore.database.models.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@EnableWebSocketSecurity
public class WebSocketSecurityConfig{

    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .simpDestMatchers("/topic/**").permitAll()
                .simpTypeMatchers(
                        SimpMessageType.CONNECT,
                        SimpMessageType.MESSAGE,
                        SimpMessageType.SUBSCRIBE).hasAuthority(Role.USER.name())

                .simpTypeMatchers(
                        SimpMessageType.UNSUBSCRIBE,
                        SimpMessageType.DISCONNECT).permitAll()
                .nullDestMatcher().authenticated()
                .anyMessage().denyAll();

        return messages.build();
    }
}
