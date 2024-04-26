package com.around.aroundcore;

import com.around.aroundcore.web.dtos.events.timepad.TimepadEvent;
import com.around.aroundcore.web.dtos.events.timepad.TimepadEvents;
import com.around.aroundcore.web.dtos.events.timepad.TimepadLocation;
import com.around.aroundcore.web.services.apis.events.TimepadAPIService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimepadAPIServiceTest {
    @Autowired
    TimepadAPIService timepadAPIService;
    @Test
    void getOneEventInEKB(){
        TimepadEvents timepadEvents = timepadAPIService.getEventsForCity(20,"Екатеринбург");
        List<TimepadEvent> events = timepadEvents.getValues();
        log.info(events.toString());
        for(TimepadEvent event : events){
            List<Double> coord = event.getLocation().getCoordinates();
            if(coord==null){
                String addr = event.getLocation().toString();
                log.info(addr);
            }else{
                log.info(coord.toString());
            }
        }
    }
    @ParameterizedTest
    @CsvSource(value = {
            "Россия:Екатеринбург:Ленина 50",
            ":Екатеринбург:Ленина 50",
            "Россия::Ленина 50",
            "Россия:Екатеринбург:",
            "Россия::",
            "::"
    }, delimiter = ':')
    void testTimepadLocation(String country, String city, String addr){
        TimepadLocation location = TimepadLocation.builder()
                .country(country)
                .city(city)
                .address(addr)
                .build();
        String excepted = "";
        if(city!=null && addr != null){
            excepted = Stream.of(country, city, addr)
                    .filter(s -> s != null && !s.isEmpty())
                    .collect(Collectors.joining(", "));
        }
        Assertions.assertEquals(excepted,location.toString());
    }
}
