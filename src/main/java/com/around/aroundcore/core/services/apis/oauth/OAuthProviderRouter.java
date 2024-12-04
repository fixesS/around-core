package com.around.aroundcore.core.services.apis.oauth;

import com.around.aroundcore.database.models.oauth.OAuthProvider;
import com.around.aroundcore.core.exceptions.api.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthProviderRouter {
    private final VkOAuthService vkOAuthService;
    private final GoogleOAuthService googleOAuthService;


    public ProviderOAuthService getProviderOAuthService(OAuthProvider provider) throws ApiException {
        return switch (provider){
            case VK -> vkOAuthService;
            case GOOGLE -> googleOAuthService;
        };
    }
}
