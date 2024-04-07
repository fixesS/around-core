package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Role;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.security.AuthService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserEmailNotUnique;
import com.around.aroundcore.web.exceptions.entity.GameUserUsernameNotUnique;
import com.around.aroundcore.web.dto.RegistrationDTO;
import com.around.aroundcore.web.dto.TokenData;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    private GameUserService userService;
    private TeamService teamService;


    @PostMapping
    @Operation(
            summary = "Registration",
            description = "Registration user by email and password"
    )
    public ResponseEntity<TokenData> registration(HttpServletRequest request, @Validated @RequestBody RegistrationDTO registrationDTO) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");//mobile-front
        String ip_address = request.getRemoteAddr();
        GameUser user = null;
        String body = "";
        ApiResponse response;
        try {
            userService.checkEmail(registrationDTO.getEmail());
            userService.checkUsername(registrationDTO.getUsername());
            Team team = teamService.findById(registrationDTO.getTeam_id());

            user = GameUser.builder()
                    .username(registrationDTO.getUsername())
                    .team(team)
                    .city(registrationDTO.getCity())
                    .level(0)
                    .coins(0)
                    .email(registrationDTO.getEmail())
                    .password(passwordEncoder.encode(registrationDTO.getPassword()))
                    .role(Role.USER)
                    .build();
            userService.create(user);

            response = ApiResponse.OK;
        } catch (GameUserEmailNotUnique e) {
            response = ApiResponse.USER_ALREADY_EXIST;
            log.error(e.getMessage());
        }catch (GameUserUsernameNotUnique e) {
            response = ApiResponse.USER_NOT_UNIQUE_USERNAME;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.TEAM_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip_address));
                return new ResponseEntity<>(tokenData, response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
}
