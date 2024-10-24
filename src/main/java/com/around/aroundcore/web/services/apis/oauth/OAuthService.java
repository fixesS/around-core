package com.around.aroundcore.web.services.apis.oauth;

import com.around.aroundcore.database.models.OAuthProvider;
import com.around.aroundcore.web.dtos.oauth.OAuthResponse;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final VkOAuthService vkOAuthService;
    private final GoogleOAuthService googleOAuthService;


    public OAuthResponse getOAuthResponse(OAuthProvider provider, String accessToken) throws ApiException {
        OAuthResponse oAuthResponse;

        switch (provider){
            case VK -> oAuthResponse =  vkOAuthService.checkToken(accessToken);
            case GOOGLE -> oAuthResponse = googleOAuthService.checkToken(accessToken);
            default -> throw new ApiException(ApiResponse.UNKNOWN_ERROR);
        }
        return oAuthResponse;
    }
}
