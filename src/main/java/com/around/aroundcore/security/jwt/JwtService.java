package com.around.aroundcore.security.jwt;

import com.around.aroundcore.security.models.Token;
import com.around.aroundcore.web.exceptions.AuthHeaderEmptyException;
import com.around.aroundcore.web.exceptions.AuthHeaderException;
import com.around.aroundcore.web.exceptions.AuthHeaderNotStartsWithPrefixException;
import com.around.aroundcore.web.exceptions.AuthHeaderNullException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class JwtService {
    public static final String AUTH_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    private static final String JWT_ACCESS = "access";
    private static final String JWT_REFRESH = "refresh";
    @Value("${jwt.token.access.secret}")
    private String accessSecretStr;
    private SecretKey accessSecret;
    @Value("${jwt.token.refresh.secret}")
    private String refreshSecretStr;
    private SecretKey refreshSecret;
    @Value("${jwt.token.access.expired}")
    private long accessExp;

    @Value("${jwt.token.refresh.expired}")
    private long refreshExp;
    private String issuer;

    @PostConstruct
    public void doInit(){
        accessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretStr));
        refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretStr));
    }
    public Token generateAccessToken(UUID sessionUuid){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+accessExp);

        Claims claims = Jwts.claims()
                .setSubject(JWT_ACCESS)
                .setIssuer(issuer)
                .setId(sessionUuid.toString())
                .setIssuedAt(now)
                .setExpiration(expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(accessSecret)
                .compact();

        return Token.builder()
                .token(token)
                .created(now)
                .expiresIn(expiration)
                .build();
    }
    public Token generateRefreshToken(UUID sessionUuid){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+refreshExp);

        Claims claims = Jwts.claims()
                .setSubject(JWT_REFRESH)
                .setIssuer(issuer)
                .setId(sessionUuid.toString())
                .setIssuedAt(now)
                .setExpiration(expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(refreshSecret)
                .compact();

        return Token.builder()
                .token(token)
                .created(now)
                .expiresIn(expiration)
                .build();
    }
    private boolean validateToken(String token, Key secret, String type) throws Exception{
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return type.equals(claims.getSubject());
    }
    public boolean validateAccessToken(String accessToken) throws Exception{
        return validateToken(accessToken,accessSecret,JWT_ACCESS);
    }
    public boolean validateRefreshToken(String refreshToken) throws Exception{
        return validateToken(refreshToken,refreshSecret,JWT_REFRESH);
    }
    public Claims getAccessClaims( String accessToken) {
        return getClaims(accessToken, accessSecret);
    }

    public Claims getRefreshClaims( String refreshToken) {
        return getClaims(refreshToken, refreshSecret);
    }

    private Claims getClaims( String token,  Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UUID getSessionIdAccess(String accessToken){
        Claims claims = getAccessClaims(accessToken);
        return UUID.fromString(claims.getId());
    }
    public UUID getSessionIdRefresh(String refreshToken){
        Claims claims = getAccessClaims(refreshToken);
        return UUID.fromString(claims.getId());

    }
    public String resolveAuthHeader(HttpServletRequest request) throws AuthHeaderNullException, AuthHeaderNotStartsWithPrefixException {
        final String authHeader = request.getHeader(AUTH_HEADER);
        if(authHeader == null ){
            throw new AuthHeaderNullException();
        }
        if(!authHeader.startsWith(JWT_PREFIX)){
            throw new AuthHeaderNotStartsWithPrefixException();
        }
        return authHeader;
    }
    public String resolveAuthHeader(ServerHttpRequest request) throws AuthHeaderException {
        HttpHeaders headers = request.getHeaders();
        final List<String> authHeader = headers.get(AUTH_HEADER);
        if(authHeader == null ){
            throw new AuthHeaderNullException();
        }
        if(authHeader.isEmpty()){
            throw new AuthHeaderEmptyException();
        }
        String authHeaderString = authHeader.get(0);
        if(!authHeaderString.startsWith(JWT_PREFIX)){
            throw new AuthHeaderNotStartsWithPrefixException();
        }
        return authHeaderString;
    }
    public String resolveAuthHeader(StompHeaderAccessor accessor) throws AuthHeaderException {
        final String authHeader = accessor.getFirstNativeHeader(AUTH_HEADER);
        if(authHeader == null ){
            throw new AuthHeaderNullException();
        }
        if(!authHeader.startsWith(JWT_PREFIX)){
            throw new AuthHeaderNotStartsWithPrefixException();
        }
        return authHeader;
    }
    public String resolveToken(HttpServletRequest request) throws AuthHeaderException {
        String authHeader = resolveAuthHeader(request);
        if (!StringUtils.hasText(authHeader)) {
            throw new AuthHeaderEmptyException();
        }
        return authHeader.substring(7);
    }
    public String resolveToken(String authHeader) throws AuthHeaderNotStartsWithPrefixException {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(JWT_PREFIX)) {
            throw new AuthHeaderNotStartsWithPrefixException();
        }
        return authHeader.substring(7);
    }
}

