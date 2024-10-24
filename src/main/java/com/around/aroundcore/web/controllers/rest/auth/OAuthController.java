package com.around.aroundcore.web.controllers.rest.auth;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.security.services.AuthService;
import com.around.aroundcore.web.dtos.auth.OAuthDTO;
import com.around.aroundcore.web.dtos.auth.TokenData;
import com.around.aroundcore.web.dtos.oauth.OAuthResponse;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.services.apis.oauth.OAuthService;
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
import java.util.Random;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_OAUTH)
@Tag(name="oAuth controller", description="Handles authentication from other provider such as VK and GOOGLE")
public class OAuthController {
    private final GameUserService gameUserService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final OAuthService oAuthService;

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
        OAuthResponse oAuthResponse = oAuthService.getOAuthResponse(oAuthDTO.getProvider(),oAuthDTO.getAccess_token());

        try{
            user = gameUserService.findByOAuthIdAndProvider(oAuthResponse.getUser_id(), oAuthDTO.getProvider());
        }catch (GameUserNullException e){
            user = createUser(oAuthResponse, oAuthDTO.getProvider());
        }
        TokenData tokenData = authService.createSession(user,userAgent, InetAddress.getByName(ip));

        return ResponseEntity.ok(tokenData);
    }


    private GameUser createUser(OAuthResponse oAuthResponse, OAuthProvider oAuthProvider){
        log.info("Creating user");
        GameUser user = GameUser.builder()
                .username(generateUsername())
                .email(oAuthResponse.getEmail())
                .password(passwordEncoder.encode(generatePassword()))
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
    private String generateUsername(){
        log.info("Creating username");
        boolean isFree = false;
        Random random = new Random();
        String username = "user@";
        while(!isFree){
            username = "user@"+random.nextInt(100000);
            isFree = !gameUserService.existByUsername(username);
        }
        return username;
    }
    private String generatePassword(){
        log.info("Creating password");
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String specialChars = "!@#%^&*()_+-=:;\"'{}<>?|`~,.";

        Random random = new Random();
        StringBuilder password = new StringBuilder();
        password.append(uppercase.charAt(random.nextInt(0,uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(0,uppercase.length())));
        for (int i = 0; i < 8; i++) {
            if(random.nextBoolean()){
                password.append(uppercase.charAt(random.nextInt(uppercase.length())));
            }else{
                password.append(lowercase.charAt(random.nextInt(lowercase.length())));
            }
        }
        password.append(random.nextInt(0,10000));
        password.append(specialChars.charAt(random.nextInt(0,specialChars.length())));
        return password.toString();
    }
}
