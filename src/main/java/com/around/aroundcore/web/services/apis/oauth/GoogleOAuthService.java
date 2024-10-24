package com.around.aroundcore.web.services.apis.oauth;

import com.around.aroundcore.web.dtos.oauth.OAuthResponse;
import com.around.aroundcore.web.dtos.oauth.google.GooglePerson;
import com.around.aroundcore.web.exceptions.oauth.GoogleOAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
        try{
            ResponseEntity<GooglePerson> response = restTemplate.getForEntity(getUserUrl, GooglePerson.class, headers);
            GooglePerson googlePerson = response.getBody();
            return OAuthResponse.builder()
                    .user_id(Long.valueOf(googlePerson.getId()))
                    .first_name(googlePerson.getGiven_name())
                    .last_name("")
                    .email("")
                    .avatar(googlePerson.getPicture())
                    .build();
        }catch (HttpClientErrorException e){
            throw new GoogleOAuthException("some error");
        } catch (NullPointerException e) {
            throw new GoogleOAuthException("null pointer");
        }
    }
}
