package com.around.aroundcore;

import com.around.aroundcore.web.services.MapEventCreationService;
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
class MapEventCreationServiceTest {
    @Autowired
    MapEventCreationService mapEventCreationService;

    @Test
    void testCreateEvents(){
        mapEventCreationService.create();
        Assertions.assertTrue(true);
    }
}
