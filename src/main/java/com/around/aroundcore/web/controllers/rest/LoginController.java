package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.security.services.AuthService;
import com.around.aroundcore.web.dtos.AuthDTO;
import com.around.aroundcore.web.dtos.TokenData;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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
    private final AuthService authService;
    private final GameUserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    @Operation(
            summary = "Login",
            description = "Auth by email and password"
    )
    @Transactional
    public ResponseEntity<TokenData> login(HttpServletRequest request, @Validated @RequestBody AuthDTO authDTO) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        GameUser user = userService.findByEmail(authDTO.getEmail());

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(authenticationToken);

            user = (GameUser) auth.getPrincipal();
        }catch (BadCredentialsException e){
            //response = ApiResponse.LOG_INCORRECT_PASSWORD_OR_LOGIN; do not show that password is incorrect
            throw new ApiException(ApiResponse.USER_DOES_NOT_EXIST);
        }
        TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip));

        return ResponseEntity.ok(tokenData);
    }
}
