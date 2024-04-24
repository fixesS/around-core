package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.services.AuthService;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.web.dtos.TokenData;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(AroundConfig.API_V1_REFRESH)
@Tag(name="Refresh controller", description="Handles refresh requests")
@AllArgsConstructor
public class RefreshController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final SessionService sessionService;

    @PostMapping
    @Operation(
            summary = "Refresh",
            description = "Refresh session by refresh_token"
    )
    public ResponseEntity<TokenData> refresh( @RequestParam("refresh_token") String refresh_token) {
        var sessionUuid = jwtService.getSessionIdRefresh(refresh_token);
        ApiResponse response;
        TokenData tokenData = null;

        try{
            jwtService.validateRefreshToken(refresh_token);
            var session = sessionService.findByUuid(sessionUuid);
            tokenData = authService.refreshSession(session.getSessionUuid());
            response = ApiResponse.OK;
        }catch (JwtException e) {
            response = ApiResponse.INVALID_TOKEN;
            log.debug(e.getMessage());
        }catch (SessionNullException e){
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(tokenData, response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
}
