package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.security.services.AuthService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.dto.AuthDTO;
import com.around.aroundcore.web.dto.TokenData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_LOGIN)
@Tag(name="Login controller", description="Handles login requests")
public class LoginController {
    private AuthService authService;

    private GameUserService userService;

    private AuthenticationManager authenticationManager;

    @PostMapping
    @Operation(
            summary = "Login",
            description = "Auth by email and password"
    )
    public ResponseEntity<TokenData> login(HttpServletRequest request, @Validated @RequestBody AuthDTO authDTO) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        String body = "";
        ApiResponse response;
        GameUser user = null;

        try {
            user = userService.findByEmail(authDTO.getEmail());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(authenticationToken);

            user = (GameUser) auth.getPrincipal();
            response = ApiResponse.OK;
            if (user == null) {
                response = ApiResponse.USER_DOES_NOT_EXIST;
            }
        }catch (GameUserNullException e){
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }catch (BadCredentialsException e){
            //response = ApiResponse.LOG_INCORRECT_PASSWORD_OR_LOGIN; do not show that password is incorrect
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip));
                return new ResponseEntity<>(tokenData,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
}
