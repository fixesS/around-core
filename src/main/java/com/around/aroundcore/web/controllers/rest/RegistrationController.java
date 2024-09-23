package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.RoundService;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.database.services.VerificationTokenService;
import com.around.aroundcore.security.services.AuthService;
import com.around.aroundcore.web.dtos.RegistrationDTO;
import com.around.aroundcore.web.dtos.TokenData;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.tasks.EmailSendingTask;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_REGISTRATION)
@Tag(name="Registration controller", description="Handles registration requests")
public class RegistrationController {
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final GameUserService userService;
    private final TeamService teamService;
    private final VerificationTokenService verificationTokenService;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final EmailSendingTask emailSendingTask;
    private final RoundService roundService;

    @PostConstruct
    public void executeSendingEmails(){
        Duration duration = Duration.of(100, TimeUnit.MILLISECONDS.toChronoUnit());
        taskScheduler.scheduleWithFixedDelay(emailSendingTask, duration);
    }

    @PostMapping
    @Operation(
            summary = "Registration",
            description = "Registration user by email and password"
    )
    @Transactional
    public ResponseEntity<TokenData> registration(HttpServletRequest request, @Validated @RequestBody RegistrationDTO registrationDTO) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");//mobile-front
        String ip_address = request.getRemoteAddr();

        userService.checkEmail(registrationDTO.getEmail());
        userService.checkUsername(registrationDTO.getUsername());
        Team team = teamService.findById(registrationDTO.getTeam_id());

        GameUser user = GameUser.builder()
                .username(registrationDTO.getUsername())
                .verified(false)
                .level(0)
                .coins(0)
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(Role.USER)
                .build();
        user.setCity(registrationDTO.getCity());
        user.setAvatar(registrationDTO.getAvatar());

        userService.create(user);
        userService.setTeamForRound(user,team,roundService.getCurrentRound());

        TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip_address));
        return ResponseEntity.ok(tokenData);
    }
    @PostMapping("/confirm")
    @Operation(
            summary = "Confirming registration",
            description = "Confirming registration with token from email message"
    )
    @Transactional
    public ResponseEntity<String> registrationConfrim(@RequestParam("token") String token) {
        VerificationToken verificationToken;
        GameUser user;

        try{
            verificationToken = verificationTokenService.findByToken(token);
            user = verificationToken.getUser();
            user.setVerified(true);
            verificationTokenService.delete(verificationToken);
            userService.update(user);

        } catch (ExpiredJwtException expEx) {
            throw new ApiException(ApiResponse.VERIFIED_TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new ApiException(ApiResponse.INVALID_TOKEN);
        }
        user = verificationToken.getUser();
        user.setVerified(true);
        verificationTokenService.delete(verificationToken);
        userService.update(user);

        return ResponseEntity.ok("");
    }
}
