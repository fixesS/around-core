package com.around.aroundcore.web.services.apis.coords;

import com.around.aroundcore.web.dtos.coords.Location;
import com.around.aroundcore.web.dtos.coords.geotree.GeotreeAddress;
import com.around.aroundcore.web.dtos.coords.geotree.GeotreeCoords;
import com.around.aroundcore.web.enums.CoordsAPIType;
import com.around.aroundcore.web.exceptions.api.CoordsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GeotreeAPIService extends CoordsAPI{
    private final RestTemplate restTemplate;
    private final String key;
    private final String mainUrl;

    public GeotreeAPIService(CoordsAPIType type, RestTemplate restTemplate, String key, String mainUrl) {
        super(type);
        this.restTemplate = restTemplate;
        this.key = key;
        this.mainUrl =mainUrl;
    }

    @Override
    public Double[] getCoordsForLocation(Location location){
        Double[] coords = new Double[2];
        if(!location.toString().equals("")){
            coords = getCoordinatesForAddress(1,location);
        }else{
            throw new RuntimeException("Address is empty");
        }
        return coords;
    }

    private Double[] getCoordinatesForAddress(Integer number, Location location) throws CoordsNotFoundException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>(headers);


        Map<String, String> variables = new HashMap<>();
        variables.put("limit",String.valueOf(number));
        variables.put("term",location.toString());
        variables.put("key",key);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(mainUrl)
                .queryParam("limit", "{limit}")
                .queryParam("term","{term}")
                .queryParam("key","{key}")
                .encode()
                .toUriString();
        Double[] coords = new Double[2];
        try{
            GeotreeCoords geotreeCoords = getGeotreeCords(urlTemplate,entity,variables);
            coords[0] = geotreeCoords.getLat();
            coords[1] = geotreeCoords.getLon();
        }catch (RuntimeException e){// trying find coords with address without country
            log.error("Coords not found in location :{}", location);
            variables.put("term",location.toStringWithoutCountry());
            try{
                GeotreeCoords geotreeCoords = getGeotreeCords(urlTemplate,entity,variables);
                coords[0] = geotreeCoords.getLat();
                coords[1] = geotreeCoords.getLon();
            }catch (RuntimeException e1){
                log.error("Coords not found in location without country :{}", location.toStringWithoutCountry());
                throw new CoordsNotFoundException();
            }
        }
        return coords;
    }
    private GeotreeCoords getGeotreeCords(String urlTemplate, HttpEntity<?> entity, Map<String, String> variables){
        ResponseEntity<GeotreeAddress[]> response = restTemplate.exchange(urlTemplate, HttpMethod.GET,entity, GeotreeAddress[].class,variables);
        if(response.getStatusCode().is2xxSuccessful() && (response.getBody() != null)){
            List<GeotreeAddress> geotreeAddresses = Arrays.stream(response.getBody()).toList();
            if(geotreeAddresses.isEmpty()){
                throw new RuntimeException("response body list is empty");
            }
            return geotreeAddresses.get(0).getGeo_center();
        }else{
            throw new RuntimeException("response body is null or response status is not ok");
        }
    }
}
