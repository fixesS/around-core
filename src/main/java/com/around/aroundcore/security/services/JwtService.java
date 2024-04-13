package com.around.aroundcore.security.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.security.models.Token;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderEmptyException;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderException;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderNotStartsWithPrefixException;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderNullException;
import com.around.aroundcore.web.exceptions.jwt.WrongJwtTypeException;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class JwtService {
    public static final String AUTH_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    private static final String JWT_ACCESS = "access";
    private static final String JWT_REFRESH = "refresh";
    private static final String JWT_VERIFICATION = "verification";
    private static final String JWT_RECOVERY = "recovery";
    @Value("${jwt.token.access.secret}")
    private String accessSecretStr;
    private SecretKey accessSecret;
    @Value("${jwt.token.refresh.secret}")
    private String refreshSecretStr;
    private SecretKey refreshSecret;
    @Value("${jwt.token.verification.secret}")
    private String verificationSecretStr;
    private SecretKey verificationSecret;
    @Value("${jwt.token.recovery.secret}")
    private String recoverySecretStr;
    private SecretKey recoverySecret;
    @Value("${jwt.token.access.expired}")
    private long accessExp;
    @Value("${jwt.token.refresh.expired}")
    private long refreshExp;
    @Value("${jwt.token.verification.expired}")
    private long verificationExp;
    @Value("${jwt.token.recovery.expired}")
    private long recoveryExp;
    private String issuer;

    @PostConstruct
    public void doInit(){
        accessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretStr));
        refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretStr));
        verificationSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(verificationSecretStr));
        recoverySecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(recoverySecretStr));
    }
    public Token generateRecoveryToken(String email){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+recoveryExp);

        Claims claims = Jwts.claims()
                .subject(JWT_RECOVERY)
                .issuer(issuer)
                .id(email)
                .issuedAt(now)
                .expiration(expiration).build();

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(recoverySecret)
                .compact();

        return Token.builder()
                .token(token)
                .created(now)
                .expiresIn(expiration)
                .build();

    }
    public Token generateVerificationToken(String email){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+verificationExp);

        Claims claims = Jwts.claims()
                .subject(JWT_VERIFICATION)
                .issuer(issuer)
                .id(email)
                .issuedAt(now)
                .expiration(expiration).build();

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(verificationSecret)
                .compact();

        return Token.builder()
                .token(token)
                .created(now)
                .expiresIn(expiration)
                .build();

    }
    public Token generateAccessToken(UUID sessionUuid){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+accessExp);

        Claims claims = Jwts.claims()
                .subject(JWT_ACCESS)
                .issuer(issuer)
                .id(sessionUuid.toString())
                .issuedAt(now)
                .expiration(expiration).build();

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
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
                .subject(JWT_REFRESH)
                .issuer(issuer)
                .id(sessionUuid.toString())
                .issuedAt(now)
                .expiration(expiration)
                .build();

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(refreshSecret)
                .compact();

        return Token.builder()
                .token(token)
                .created(now)
                .expiresIn(expiration)
                .build();
    }
    private void validateToken(String token, SecretKey secret, String type) {
        Claims claims = Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if(!type.equals(claims.getSubject())){
            String errmsg = String.format("Wrong JWT type: %s",claims.getSubject());
            throw new WrongJwtTypeException(errmsg);
        }
    }
    public void validateRecoveryToken(String verificationToken){
        validateToken(verificationToken,recoverySecret,JWT_RECOVERY);
    }
    public void validateVerificationToken(String verificationToken){
        validateToken(verificationToken,verificationSecret,JWT_VERIFICATION);
    }
    public void validateAccessToken(String accessToken) {
        validateToken(accessToken,accessSecret,JWT_ACCESS);
    }
    public void validateRefreshToken(String refreshToken) {
        validateToken(refreshToken,refreshSecret,JWT_REFRESH);
    }
    public Claims getVerificationClaims( String verificationToken) {
        return getClaims(verificationToken, verificationSecret);
    }
    public Claims getAccessClaims( String accessToken) {
        return getClaims(accessToken, accessSecret);
    }

    public Claims getRefreshClaims( String refreshToken) {
        return getClaims(refreshToken, refreshSecret);
    }

    private Claims getClaims( String token,  SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
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

