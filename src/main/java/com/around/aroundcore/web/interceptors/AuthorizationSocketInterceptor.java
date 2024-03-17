package com.around.aroundcore.web.interceptors;

import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.jwt.JwtAuthenticationToken;
import com.around.aroundcore.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
public class AuthorizationSocketInterceptor implements ChannelInterceptor {

    private SessionService sessionService;
    private JwtService jwtService;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(!(nonNull(accessor))){
            log.info("null accessor");
            return null;
        }
        switch (Objects.requireNonNull(accessor.getCommand())) {
            case SEND,SUBSCRIBE -> {
                String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                if (!isNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                    val accessToken = authorizationHeader.substring("Bearer".length() + 1);
                    if (jwtService.validateAccessToken(accessToken)) {
                        Session session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));

                        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(session, session.getUser());
                        jwtAuthenticationToken.setAuthenticated(true);

                        accessor.setUser(jwtAuthenticationToken);
                        log.debug("headers:"+message.getHeaders());
                        return message;
                    } else {
                        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.SEND);
                        headerAccessor.setMessage("ИДИ НВХУЙ СО СВОИМ ЖВТ JWT invalid");

                        channel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
                        log.info("WS JWT invalid");
                        log.info("headers:"+message.getHeaders());
                        return null;
                    }
                } else {
                    log.info("WS AUTH header is null or invalid");
                    log.info("headers:"+message.getHeaders());
                    return null;
                }
            }
            default -> {

                log.info("MSGMSGMSG:"+message.toString());
                log.info("msfmfmfsjf headers:"+message.getHeaders());
                return message;
            }
        }
    }
}
