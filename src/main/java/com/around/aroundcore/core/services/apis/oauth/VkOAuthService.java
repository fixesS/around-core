package com.around.aroundcore.core.services.apis.oauth;

import com.around.aroundcore.web.dtos.oauth.OAuthResponse;
import com.around.aroundcore.web.dtos.oauth.vk.VkUserModel;
import com.around.aroundcore.web.dtos.oauth.vk.VkUserModelWrapper;
import com.around.aroundcore.core.exceptions.api.oauth.OAuthException;
import com.around.aroundcore.core.exceptions.api.oauth.VkOAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class VkOAuthService implements ProviderOAuthService {
    private final RestTemplate restTemplate;

    @Value("${around.oauth.url.vk}")
    private String checkTokenUrl;
    @Value("${around.oauth.client_id.vk}")
    private String clientId;

    @Override
    public OAuthResponse checkToken(String token) throws OAuthException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("access_token", token);
        body.add("client_id", clientId);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try{
            ResponseEntity<VkUserModelWrapper> response = restTemplate.exchange(checkTokenUrl, HttpMethod.POST, requestEntity, VkUserModelWrapper.class);
            VkUserModel vkUserModel = response.getBody().getUser();
            return OAuthResponse.builder()
                    .user_id(vkUserModel.getUser_id())
                    .first_name(vkUserModel.getFirst_name().toLowerCase())
                    .last_name(vkUserModel.getLast_name().toLowerCase())
                    .email(vkUserModel.getEmail().toLowerCase())
                    .avatar(vkUserModel.getAvatar())
                    .build();
        }catch (HttpClientErrorException e){
            throw new VkOAuthException("some error");
        } catch (NullPointerException e) {
            throw new VkOAuthException("null pointer");
        }
    }
}
