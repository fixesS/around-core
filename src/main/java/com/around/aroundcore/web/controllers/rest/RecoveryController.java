package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.RecoveryToken;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.RecoveryTokenService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.dtos.ForgotPasswordDTO;
import com.around.aroundcore.web.dtos.ResetPasswordDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.events.OnPasswordRecoveryEvent;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.tasks.CheckTokensTask;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_RECOVERY)
@Tag(name="Recovery controller", description="Handles requests for recovery access to user account")
public class RecoveryController {
    private final ApplicationEventPublisher eventPublisher;
    private final RecoveryTokenService recoveryTokenService;
    private final GameUserService userService;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final CheckTokensTask checkTokensTask;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    @PostConstruct
    public void executeSendingEmails(){
        Duration duration = Duration.of(10, TimeUnit.MINUTES.toChronoUnit());
        taskScheduler.scheduleWithFixedDelay(checkTokensTask, duration);
    }
    @PostMapping("/forgotPassword")
    @Operation(
            summary = "Sends email message",
            description = "Sends email message to recover password"
    )
    @Transactional
    public ResponseEntity<String> forgotPassword(@Validated @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        GameUser user = userService.findByEmail(forgotPasswordDTO.getEmail());
        if(Boolean.FALSE.equals(user.getVerified())){
            throw new ApiException(ApiResponse.USER_IS_NOT_VERIFIED);
        }else{
            eventPublisher.publishEvent(new OnPasswordRecoveryEvent(user));
        }

        return ResponseEntity.ok("");
    }
    @PostMapping("/resetPassword")
    @Operation(
            summary = "Resets user password",
            description = "Resets user password with token from email message"
    )
    @Transactional
    public ResponseEntity<String> resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO, @RequestParam("token") String token ) {
        RecoveryToken recoveryToken;

        try{
            recoveryToken = recoveryTokenService.findByToken(token);
        } catch (ExpiredJwtException expEx) {
            throw new ApiException(ApiResponse.RECOVERY_TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new ApiException(ApiResponse.INVALID_TOKEN);
        }
        var user = recoveryToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        recoveryTokenService.delete(recoveryToken);
        sessionService.deleteAllByGameUser(user);
        userService.update(user);

        return ResponseEntity.ok("");
    }
}
