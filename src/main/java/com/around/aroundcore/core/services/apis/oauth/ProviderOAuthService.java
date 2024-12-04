package com.around.aroundcore.core.services.apis.oauth;

import com.around.aroundcore.web.dtos.oauth.OAuthResponse;

public interface ProviderOAuthService {
    OAuthResponse checkToken(String token);
}
