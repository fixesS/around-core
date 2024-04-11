package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.VerificationToken;
import com.around.aroundcore.database.repositories.VerificationTokenRepository;
import com.around.aroundcore.security.models.Token;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.VerificationTokenNullException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class VerificationTokenService {
    private VerificationTokenRepository tokenRepository;
    private JwtService jwtService;
    @Transactional
    public VerificationToken createTokenWithUser(GameUser user) {
        Token token = jwtService.generateVerificationToken(user.getEmail());
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token.getToken())
                .user(user)
                .created(token.getCreated().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .expiresIn(token.getExpiresIn().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .build();
        tokenRepository.save(verificationToken);
        return verificationToken;
    }
    @Transactional
    public VerificationToken findById(Integer id) throws VerificationTokenNullException {
        VerificationToken verificationToken =  tokenRepository.findById(id).orElseThrow(VerificationTokenNullException::new);
        validateToken(verificationToken);
        return verificationToken;
    }
    @Transactional
    public VerificationToken findByToken(String token) throws VerificationTokenNullException {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElseThrow(VerificationTokenNullException::new);
        validateToken(verificationToken);
        return verificationToken;
    }

    @Transactional
    public void delete(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }
    private void validateToken(VerificationToken verificationToken){
        String token = verificationToken.getToken();
        jwtService.validateVerificationToken(token);
    }
}
