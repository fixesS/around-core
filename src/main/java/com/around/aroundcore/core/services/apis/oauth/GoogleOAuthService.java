package com.around.aroundcore.core.services.apis.oauth;

import com.around.aroundcore.web.dtos.oauth.OAuthResponse;
import com.around.aroundcore.web.dtos.oauth.google.GooglePerson;
import com.around.aroundcore.core.exceptions.api.oauth.GoogleOAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService implements ProviderOAuthService{
    private final RestTemplate restTemplate;

    @Value("${around.oauth.url.google}")
    private String getUserUrl;

    @Override
    public OAuthResponse checkToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try{
            ResponseEntity<GooglePerson> response = restTemplate.exchange(getUserUrl, HttpMethod.GET, requestEntity, GooglePerson.class);
            GooglePerson googlePerson = response.getBody();
            log.info(googlePerson.toString());
            return OAuthResponse.builder()
                    .user_id(googlePerson.getId())
                    .first_name(googlePerson.getGiven_name().toLowerCase())
                    .last_name("")
                    .email(googlePerson.getEmail().toLowerCase())
                    .avatar(googlePerson.getPicture())
                    .build();
        }catch (HttpClientErrorException e){
            throw new GoogleOAuthException("some error");
        } catch (NullPointerException e) {
            throw new GoogleOAuthException("null pointer");
        }
    }
}
