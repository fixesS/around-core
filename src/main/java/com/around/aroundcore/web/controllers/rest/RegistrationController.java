package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Role;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.security.AuthService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.models.ApiError;
import com.around.aroundcore.web.models.ApiOk;
import com.around.aroundcore.web.models.RegistrationModel;
import com.around.aroundcore.web.models.TokenData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_REGISTRATION)
public class RegistrationController {
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    private GameUserService userService;

    private GsonParser gsonParser;

    @PostMapping
    public ResponseEntity<String> handle(HttpServletRequest request, @Validated @RequestBody RegistrationModel registrationModel) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");//mobile-front
        String ip_address = request.getRemoteAddr();
        GameUser user =  userService.findByEmail(registrationModel.getUsername());
        String body = "";
        ApiResponse response;
        try {
            if(user==null){
                user = GameUser.builder()
                        .username(registrationModel.getUsername())
                        .email(registrationModel.getEmail())
                        .password(passwordEncoder.encode(registrationModel.getPassword()))
                        .role(Role.USER)
                        .build();
                userService.create(user);

                response = ApiResponse.OK;
            }else{
                response = ApiResponse.USER_ALREADY_EXIST;
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip_address));
                ApiOk<TokenData> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), tokenData);
                body = gsonParser.toJson(apiOk);
            }
            default -> {
                ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.toJson(apiError);
            }
        }

        return new ResponseEntity<>(body, response.getStatus());
    }
}
