package com.around.aroundcore.security.jwt;

import com.around.aroundcore.security.models.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtService {
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
                .setSubject("access")
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
                .setSubject("refresh")
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
    private boolean validateToken(String token, Key secret, String type) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return type.equals(claims.getSubject());
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }
    public boolean validateAccessToken(String accessToken){
        return validateToken(accessToken,accessSecret,"access");
    }
    public boolean validateRefreshToken(String refreshToken){
        return validateToken(refreshToken,refreshSecret,"refresh");
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
    public String resolveToken(HttpServletRequest request){
        final String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}

