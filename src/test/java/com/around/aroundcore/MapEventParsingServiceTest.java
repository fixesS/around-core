package com.around.aroundcore;

import com.around.aroundcore.database.models.event.MapEvent;
import com.around.aroundcore.core.exceptions.api.EventsNotFoundException;
import com.around.aroundcore.core.services.MapEventParsingService;
import com.around.aroundcore.database.services.event.MapEventService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@Slf4j
@ActiveProfiles("testdadata")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MapEventParsingServiceTest {

    @Autowired
    MapEventService service;

    @Autowired
    MapEventParsingService mapEventParsingService;

    @Test
    void testParseEvents(){
//        List<MapEvent> events = null;
//        try{
//            events = mapEventParsingService.parseEvents();
//        }catch (EventsNotFoundException e){
//            log.info("Events not found");
//        }
//        Assertions.assertNotNull(events);
//        for(MapEvent event: events){
//            log.info(event.toString());
//        }
        service.createFromEventAPI();
    }
}
