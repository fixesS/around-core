package com.around.aroundcore.web.controllers.rest.auth;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.security.services.AuthService;
import com.around.aroundcore.web.dtos.auth.RegistrationDTO;
import com.around.aroundcore.web.dtos.auth.TokenData;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    private final RoundService roundService;
    private final CityService cityService;

    @PostMapping
    @Operation(
            summary = "Registration",
            description = "Registration user by email and password"
    )
    @Transactional
    public ResponseEntity<TokenData> registration(HttpServletRequest request, @Validated @RequestBody RegistrationDTO registrationDTO) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");//mobile-front
        String ip = request.getRemoteAddr();

        userService.checkEmail(registrationDTO.getEmail());
        userService.checkUsername(registrationDTO.getUsername());

        GameUser user = GameUser.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);
        if(registrationDTO.getTeam_id() != null && registrationDTO.getCity_id() != null){
            var team = teamService.findById(registrationDTO.getTeam_id());
            var city = cityService.findById(registrationDTO.getCity_id());
            userService.createTeamCityForRound(user,team,city,roundService.getCurrentRound());
        }

        TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip));
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
