package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.security.AuthService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.models.AuthModel;
import com.around.aroundcore.web.models.TokenData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    private GameUserService userService;

    private GsonParser gsonParser;

    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<String> handle(HttpServletRequest request, @RequestBody AuthModel authModel) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");
        String ip_address = request.getRemoteAddr();
        GsonParser gsonParser = new GsonParser();
        String body = "";
        ApiResponse response;
        GameUser user = userService.findByUsername(authModel.getUsername());

        try {
            if (user == null) {
                response = ApiResponse.USER_DOES_NOT_EXIST;
            } else {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(authModel.getUsername(), authModel.getPassword());
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
            //response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip_address));
                //ApiOk<TokenData> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), tokenData);
                body = gsonParser.toJson(tokenData);
            }
            default -> {
                //ApiError apiError = ApiResponse.getApiError(response.getStatusCode(),response.getMessage());
                body = gsonParser.toJson(response.getMessage());
            }

        }
        return new ResponseEntity<>(body,response.getStatus());
    }
}
