package com.around.aroundcore.web.interceptors;

import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.security.services.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
@Deprecated
public class AuthorizationSocketInterceptor implements ChannelInterceptor {

    private SessionService sessionService;
    private JwtService jwtService;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(!(nonNull(accessor))){
            log.error("null accessor");
            return null;
        }
        if(accessor.isHeartbeat()){
            return message;
        }
        if (Objects.requireNonNull(accessor.getCommand()) == StompCommand.SUBSCRIBE) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) message.getHeaders().get("simpUser");
            UUID sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
            Session session = sessionService.findByUuid(sessionUuid);
            log.info("USER:" + session.getUser().getEmail());
            log.info("msfmfmfsjf headers:" + message.getHeaders());
            accessor.setHeader("sessionUuid", sessionUuid);
            return message;
        }
        return message;
    }
    private void sendMesasge(MessageChannel channel, String message){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.SEND);
        headerAccessor.setMessage(message);
        channel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
    }
}
