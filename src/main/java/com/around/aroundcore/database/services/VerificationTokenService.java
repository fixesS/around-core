package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.token.VerificationToken;
import com.around.aroundcore.database.repositories.VerificationTokenRepository;
import com.around.aroundcore.security.models.Token;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.web.exceptions.api.entity.VerificationTokenNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class VerificationTokenService {
    private VerificationTokenRepository tokenRepository;
    private JwtService jwtService;
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
    public VerificationToken findById(Integer id) throws VerificationTokenNullException {
        VerificationToken verificationToken =  tokenRepository.findById(id).orElseThrow(VerificationTokenNullException::new);
        jwtService.validateVerificationToken(verificationToken.getToken());
        return verificationToken;
    }
    public VerificationToken findByToken(String token) throws VerificationTokenNullException {
        jwtService.validateVerificationToken(token);
        return tokenRepository.findByToken(token).orElseThrow(VerificationTokenNullException::new);
    }
    public void delete(VerificationToken token) {
        tokenRepository.delete(token);
    }
    public void removeExpired(){
        List<VerificationToken> tokens = tokenRepository.findAll();
        tokens.forEach(token ->{
            if(LocalDateTime.now().isAfter(token.getExpiresIn())){
                delete(token);
            }
        });
    }
}
