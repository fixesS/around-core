package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Role;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.security.AuthService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.models.RegistrationModel;
import com.around.aroundcore.web.models.TokenData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(AroundConfig.API_V1_REGISTRATION)
public class RegistrationController {//todo

    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    private GameUserService userService;

    private GsonParser gsonParser;

    @PostMapping
    public ResponseEntity<String> handle(HttpServletRequest request, @Validated @RequestBody RegistrationModel registrationModel) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");//mobile-front
        String ip_address = request.getRemoteAddr();
        GameUser user = null;
        String body = "";
        ApiResponse response;
        try {
            user = GameUser.builder()
                    .username(registrationModel.getUsername())
                    .password(passwordEncoder.encode(registrationModel.getPassword()))
                    .role(Role.USER)
                    .build();

            userService.create(user);

            response = ApiResponse.OK;
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            //response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip_address));
                body = gsonParser.toJson(tokenData);
            }
            default -> {
                body = gsonParser.toJson(response.getMessage());
            }
        }

        return new ResponseEntity<>(body, response.getStatus());
    }


}
