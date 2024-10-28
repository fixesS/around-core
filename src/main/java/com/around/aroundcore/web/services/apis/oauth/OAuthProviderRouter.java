package com.around.aroundcore.web.services.apis.oauth;

import com.around.aroundcore.database.models.OAuthProvider;
import com.around.aroundcore.web.exceptions.api.ApiException;
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
