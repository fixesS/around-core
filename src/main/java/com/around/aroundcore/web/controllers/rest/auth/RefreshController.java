package com.around.aroundcore.web.controllers.rest.auth;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.services.AuthService;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.web.dtos.auth.TokenData;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
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

import java.util.UUID;

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
        UUID sessionUuid;
        TokenData tokenData;

        try{
            sessionUuid = jwtService.getSessionIdRefresh(refresh_token);
            jwtService.validateRefreshToken(refresh_token);
        }catch (JwtException e) {
            throw new ApiException(ApiResponse.INVALID_TOKEN);
        }

        var session = sessionService.findByUuid(sessionUuid);
        tokenData = authService.refreshSession(session.getSessionUuid());
        return ResponseEntity.ok(tokenData);
    }
}
