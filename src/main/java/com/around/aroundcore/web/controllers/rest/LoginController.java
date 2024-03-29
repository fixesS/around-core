package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.security.AuthService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.ApiException;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.dto.ApiError;
import com.around.aroundcore.web.dto.ApiOk;
import com.around.aroundcore.web.dto.AuthDTO;
import com.around.aroundcore.web.dto.TokenData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class LoginController {
    private AuthService authService;

    private GameUserService userService;

    private GsonParser gsonParser;

    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<String> handle(HttpServletRequest request, @Validated @RequestBody AuthDTO authDTO) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");
        String ip_address = request.getRemoteAddr();
        String body = "";
        ApiResponse response;
        GameUser user = userService.findByEmail(authDTO.getEmail());

        try {
            if (user == null) {
                response = ApiResponse.USER_DOES_NOT_EXIST;
            } else {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword());
                Authentication auth = authenticationManager.authenticate(authenticationToken);

                user = (GameUser) auth.getPrincipal();
                response = ApiResponse.OK;
                if (user == null) {
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
            }
        }catch (Exception e){
            response = ApiResponse.UNKNOWN_ERROR;
            if(e instanceof BadCredentialsException){
                response = ApiResponse.USER_DOES_NOT_EXIST;
            }
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip_address));
                ApiOk<TokenData> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), tokenData);
                body = gsonParser.toJson(apiOk);
            }
            default -> throw new ApiException(response);
        }
        return new ResponseEntity<>(body,response.getStatus());
    }
}
