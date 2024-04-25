package com.around.aroundcore;

import com.around.aroundcore.web.dtos.coords.Location;
import com.around.aroundcore.web.exceptions.api.CoordsNotFoundException;
import com.around.aroundcore.web.services.apis.coords.CoordsAPI;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@Slf4j
//@ActiveProfiles("testgeotree")
@ActiveProfiles("testdadata")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoordsAPITest {

    @Autowired
    CoordsAPI coordsAPI;

    @Test
    void getCoordsForEventLocation(){
        Location location = Location.builder()
                .country("Россия")
                .city("Екатеринбург")
                .address("Ленина 50")
                .build();
        Double[] coords = new Double[2];
        try{
            coords = coordsAPI.getCoordsForLocation(location);
            log.info("'{} API' found coordinates {} for location '{}' ",coordsAPI.getType(),Arrays.toString(coords),location.toString());
        }catch (CoordsNotFoundException e){
            log.info("'{} API' could not find any coordinates for location '{}'",coordsAPI.getType(),location);
        }
    }
}
