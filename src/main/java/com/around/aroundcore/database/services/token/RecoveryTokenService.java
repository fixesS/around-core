package com.around.aroundcore.database.services.token;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.token.RecoveryToken;
import com.around.aroundcore.database.repositories.token.RecoveryTokenRepository;
import com.around.aroundcore.security.models.Token;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.core.exceptions.api.entity.RecoveryTokenNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RecoveryTokenService {
    private RecoveryTokenRepository tokenRepository;
    private JwtService jwtService;

    public RecoveryToken createTokenWithUser(GameUser user) {
        Token token = jwtService.generateRecoveryToken(user.getEmail());
        RecoveryToken recoveryToken = RecoveryToken.builder()
                .token(token.getToken())
                .user(user)
                .created(token.getCreated().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .expiresIn(token.getExpiresIn().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .build();
        tokenRepository.save(recoveryToken);
        return recoveryToken;
    }
    public RecoveryToken findById(Integer id) throws RecoveryTokenNullException {
        RecoveryToken recoveryToken = tokenRepository.findById(id).orElseThrow(RecoveryTokenNullException::new);
        jwtService.validateRecoveryToken(recoveryToken.getToken());
        return recoveryToken;
    }
    public RecoveryToken findByToken(String token) throws RecoveryTokenNullException {
        jwtService.validateRecoveryToken(token);
        return tokenRepository.findByToken(token).orElseThrow(RecoveryTokenNullException::new);
    }
    public void delete(RecoveryToken token) {
        tokenRepository.delete(token);
    }

    public void removeExpired(){
        List<RecoveryToken> tokens = tokenRepository.findAll();
        tokens.forEach(token ->{
            if(LocalDateTime.now().isAfter(token.getExpiresIn())){
                delete(token);
            }
        });
    }
}
