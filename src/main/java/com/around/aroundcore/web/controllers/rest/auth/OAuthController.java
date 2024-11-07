package com.around.aroundcore.web.controllers.rest.auth;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.security.services.AuthService;
import com.around.aroundcore.web.dtos.auth.OAuthDTO;
import com.around.aroundcore.web.dtos.auth.TokenData;
import com.around.aroundcore.web.dtos.oauth.OAuthResponse;
import com.around.aroundcore.web.exceptions.api.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.api.oauth.OAuthException;
import com.around.aroundcore.web.exceptions.api.validation.EmailValidationException;
import com.around.aroundcore.web.exceptions.api.validation.ValidationException;
import com.around.aroundcore.web.services.apis.oauth.OAuthProviderRouter;
import com.around.aroundcore.web.services.apis.oauth.ProviderOAuthService;
import com.around.aroundcore.web.services.apis.oauth.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_OAUTH)
@Tag(name="oAuth controller", description="Handles authentication from other provider such as VK and GOOGLE")
public class OAuthController {
    private final GameUserService gameUserService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final AuthService authService;
    private final OAuthProviderRouter oAuthProviderRouter;

    @PostMapping
    @Operation(
            summary = "Auth with oauth2",
            description = "Checks your access_token in provider, then if user isn't registered, create user account. Finally gives tokens."
    )
    @Transactional
    public ResponseEntity<TokenData> loginWithOAuth(HttpServletRequest request, @RequestBody OAuthDTO oAuthDTO) throws UnknownHostException {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        GameUser user;
        ProviderOAuthService providerOAuthService = oAuthProviderRouter.getProviderOAuthService(oAuthDTO.getProvider());
        OAuthResponse oAuthResponse = providerOAuthService.checkToken(oAuthDTO.getAccess_token());

        try{
            validationService.validateEmail(oAuthResponse.getEmail());
            user = gameUserService.findByOAuthIdAndProvider(oAuthResponse.getUser_id(), oAuthDTO.getProvider());
        }catch (GameUserNullException e){
            if(oAuthDTO.getProvider().equals(OAuthProvider.GOOGLE) && gameUserService.existByEmail(oAuthResponse.getEmail())){
                user = gameUserService.findByEmail(oAuthResponse.getEmail());
                addOAuthToUser(user, oAuthResponse, oAuthDTO.getProvider());
            }else{
                user = createUser(oAuthResponse, oAuthDTO.getProvider());
            }
        } catch (ValidationException e) {
            throw new OAuthException("validation exception");
        }
        TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip));

        return ResponseEntity.ok(tokenData);
    }
    private void addOAuthToUser(GameUser user, OAuthResponse oAuthResponse, OAuthProvider oAuthProvider) {
        OAuthUser oAuthUser = OAuthUser.builder()
                .oauthId(oAuthResponse.getUser_id())
                .oAuthUserEmbedded(new OAuthUserEmbedded(oAuthProvider, user))
                .build();
        user.addOAuthToUser(oAuthUser);
        gameUserService.update(user);
    }
    private GameUser createUser(OAuthResponse oAuthResponse, OAuthProvider oAuthProvider){
        log.info("Creating user");
        String username = switch (oAuthProvider){
            case GOOGLE -> gameUserService.generateUsername(oAuthResponse.getEmailWithoutDomain());
            default -> gameUserService.generateUsername("user");
        };
        GameUser user = GameUser.builder()
                .username(username)
                .email(oAuthResponse.getEmail())
                .password(passwordEncoder.encode(gameUserService.generatePassword()))
                .role(Role.USER)
                .build();
        OAuthUser oAuthUser = OAuthUser.builder()
                .oauthId(oAuthResponse.getUser_id())
                .oAuthUserEmbedded(new OAuthUserEmbedded(oAuthProvider, user))
                .build();
        user.addOAuthToUser(oAuthUser);
        gameUserService.create(user);
        return user;
    }
}
