package com.around.aroundcore.web.services.apis.events;

import com.around.aroundcore.web.dtos.events.timepad.TimepadEvents;
import com.around.aroundcore.web.exceptions.api.EventsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TimepadAPIService {
    @Value("${timepad.api.token}")
    private String token;
    @Value("${timepad.api.url}")
    private String mainUrl;
    RestTemplate restTemplate;

    public TimepadAPIService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public TimepadEvents getEventsForCity(Integer number, String city) throws EventsNotFoundException{
        if(number<1){
            number =1;
        }
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer "+token);
        headers.add("Content-Type", "application/json");


        HttpEntity<?> entity = new HttpEntity<>(headers);


        Map<String, String> variables = new HashMap<>();
        variables.put("limit",String.valueOf(number));
        variables.put("cities",city);
        variables.put("fields","location");
        variables.put("sort","+starts_at");
        variables.put("moderation_statuses","featured");

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(mainUrl)
                .queryParam("limit", "{limit}")
                .queryParam("cities","{cities}")
                .queryParam("fields","{fields}")
                .queryParam("sort","{sort}")
                .queryParam("moderation_statuses","{moderation_statuses}")
                .encode()
                .toUriString();
        ResponseEntity<TimepadEvents> response = restTemplate.exchange(urlTemplate, HttpMethod.GET,entity, TimepadEvents.class,variables);
        if(response.getStatusCode().is2xxSuccessful()){
            if(response.getBody()!=null && response.getBody().getValues()!= null && !response.getBody().getValues().isEmpty()){
                return response.getBody();
            }
        }
        throw new EventsNotFoundException();
    }
}
