package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.RecoveryToken;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.RecoveryTokenService;
import com.around.aroundcore.web.dto.ForgotPasswordDTO;
import com.around.aroundcore.web.dto.ResetPasswordDTO;
import com.around.aroundcore.web.dto.TokenData;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.events.OnPasswordRecoveryEvent;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.GameUserPasswordSame;
import com.around.aroundcore.web.exceptions.entity.RecoveryTokenNullException;
import com.around.aroundcore.web.tasks.CheckTokensTask;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_RECOVERY)
@Tag(name="Recovery controller", description="Handles requests for recovery access to user account")
public class RecoveryController {
    private ApplicationEventPublisher eventPublisher;
    private RecoveryTokenService recoveryTokenService;
    private GameUserService userService;
    private ThreadPoolTaskScheduler taskScheduler;
    private CheckTokensTask checkTokensTask;
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void executeSendingEmails(){
        Duration duration = Duration.of(30, TimeUnit.SECONDS.toChronoUnit());
        taskScheduler.scheduleWithFixedDelay(checkTokensTask, duration);
    }
    @PostMapping("/forgotPassword")
    @Operation(
            summary = "Sends email message",
            description = "Sends email message to recover password"
    )
    public ResponseEntity<String> forgotPassword(@Validated @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        ApiResponse response = ApiResponse.OK;
        GameUser user = null;

        try {
            user = userService.findByEmail(forgotPasswordDTO.getEmail());
            eventPublisher.publishEvent(new OnPasswordRecoveryEvent(user));
        }catch (GameUserNullException e){
            log.debug(e.getMessage());
        }
        //The response is always OK so as not to transfer information about an existing email to intruders
        return new ResponseEntity<>(response.getMessage(), response.getStatus());
    }
    @PostMapping("/resetPassword")
    @Operation(
            summary = "Resets user password",
            description = "Resets user password with token from email message"
    )
    public ResponseEntity<String> resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO, @RequestParam("token") String token ) {
        ApiResponse response;
        RecoveryToken recoveryToken;
        GameUser user = null;

        try{
            recoveryToken = recoveryTokenService.findByToken(token);
            user = recoveryToken.getUser();
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
            recoveryTokenService.delete(recoveryToken);
            userService.update(user);
            response = ApiResponse.OK;
        } catch (GameUserPasswordSame e) {
            response = ApiResponse.USER_NEW_PASSWORD_THE_SAME;
            log.error(e.getMessage());
        } catch (RecoveryTokenNullException e) {
            response = ApiResponse.INVALID_TOKEN;
            log.error(e.getMessage());
        } catch (ExpiredJwtException expEx) {
            response = ApiResponse.VERIFIED_TOKEN_EXPIRED;
            log.debug(expEx.getMessage());
        } catch (JwtException e) {
            response = ApiResponse.INVALID_TOKEN;
            log.debug(e.getMessage());
        }


        switch (response){
            case OK -> {
                return new ResponseEntity<>(response.getMessage(), response.getStatus());
            }
            default -> throw new ApiException(response);
        }

    }
}