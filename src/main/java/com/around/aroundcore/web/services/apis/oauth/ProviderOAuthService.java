package com.around.aroundcore.web.services.apis.oauth;

import com.around.aroundcore.web.dtos.oauth.OAuthResponse;

public interface ProviderOAuthService {
    OAuthResponse checkToken(String token);
}
