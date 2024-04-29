package com.around.aroundcore.web.services.apis.coords;

import com.around.aroundcore.web.dtos.coords.Location;
import com.around.aroundcore.web.enums.CoordsAPIType;
import com.around.aroundcore.web.exceptions.api.CoordsNotFoundException;
import com.kuliginstepan.dadata.client.DadataClient;
import com.kuliginstepan.dadata.client.domain.Suggestion;
import com.kuliginstepan.dadata.client.domain.address.Address;
import com.kuliginstepan.dadata.client.domain.address.AddressRequestBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DadataAPIService extends CoordsAPI{
    private final DadataClient dadataClient;

    public DadataAPIService(CoordsAPIType type, DadataClient dadataClient) {
        super(type);
        this.dadataClient = dadataClient;
    }

    @Override
    public Double[] getCoordsForLocation(Location location) throws CoordsNotFoundException{
        Address addressObj = getAddress(location.toStringWithoutCountry());
        Double[] coords = new Double[2];
        if(addressObj != null && (addressObj.getGeoLat()!=null && addressObj.getGeoLon()!=null)){
                coords[0] = addressObj.getGeoLat();
                coords[1] = addressObj.getGeoLon();
                return coords;
        }
        log.error("Coords not found in location :{}", location.toStringWithoutCountry());
        throw new CoordsNotFoundException();
    }
    public Address getAddress(String addressAsString){
        List<Suggestion<Address>> addresses = dadataClient.suggestAddress(AddressRequestBuilder.create(addressAsString).build()).collectList().block();
        if(addresses == null || addresses.isEmpty() ||addresses.get(0) == null){
            log.error(addresses.toString());
            return null;
        }
        return addresses.get(0).getData();
    }
}
