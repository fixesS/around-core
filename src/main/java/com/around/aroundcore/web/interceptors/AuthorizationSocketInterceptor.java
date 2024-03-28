package com.around.aroundcore.web.interceptors;

import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.jwt.JwtAuthenticationToken;
import com.around.aroundcore.security.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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
            log.error("null accessor");
            return null;
        }
        if(accessor.isHeartbeat()){
            return message;
        }
        switch (Objects.requireNonNull(accessor.getCommand())) {
            case SEND-> {
                String authorizationHeader = jwtService.resolveAuthHeader(accessor);
                if(authorizationHeader == null){

                }

                if (!isNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                    val accessToken = authorizationHeader.substring("Bearer".length() + 1);
                    try{
                        jwtService.validateAccessToken(accessToken);
                    } catch (ExpiredJwtException expEx) {
                        sendMesasge(channel,"Token expired");
                        log.info("Token expired", expEx);
                        return null;
                    } catch (UnsupportedJwtException unsEx) {
                        sendMesasge(channel,"Unsupported jwt");
                        log.info("Unsupported jwt", unsEx);
                        return null;
                    } catch (MalformedJwtException mjEx) {
                        sendMesasge(channel,"Malformed jwt");
                        log.info("Malformed jwt", mjEx);
                        return null;
                    } catch (Exception e) {
                        sendMesasge(channel,"Invalid token");
                        log.info("Invalid token", e);
                        return null;
                    }
                    Session session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));

                    JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(session, session.getUser());
                    jwtAuthenticationToken.setAuthenticated(true);

                    accessor.setUser(jwtAuthenticationToken);
                    log.debug("headers:"+message.getHeaders());
                    return message;
                } else {
                    log.error("WS AUTH header is null or invalid");
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
    private void sendMesasge(MessageChannel channel, String message){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.SEND);
        headerAccessor.setMessage(message);
        channel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
    }
}
