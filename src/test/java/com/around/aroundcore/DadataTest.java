package com.around.aroundcore;

import com.around.aroundcore.web.dtos.events.timepad.TimepadEvent;
import com.around.aroundcore.web.dtos.events.timepad.TimepadLocation;
import com.kuliginstepan.dadata.client.DadataClient;
import com.kuliginstepan.dadata.client.domain.Suggestion;
import com.kuliginstepan.dadata.client.domain.address.Address;
import com.kuliginstepan.dadata.client.domain.address.AddressRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DadataTest {
    @Autowired
    DadataClient dadataClient;

    @Test
    void getCoordsForEventLocation(){
        TimepadEvent event = TimepadEvent.builder()
                .location(TimepadLocation.builder()
                        .country("Россия")
                        .city("Екатеринбург")
                        .address("Ленина 50")
                        .build())
                .build();

        List<Suggestion<Address>> addresses = dadataClient.suggestAddress(AddressRequestBuilder.create(event.getLocation().toString()).build()).collectList().block();
        assert addresses != null;
        Address address = addresses.get(0).getData();
        Double[] coords = new Double[2];
        coords[0] = address.getGeoLat();
        coords[1] = address.getGeoLon();
        log.info(Arrays.stream(coords).toList().toString());
    }
}
