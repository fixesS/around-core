package com.around.aroundcore;

import com.around.aroundcore.web.services.apis.coords.CoordsAPI;
import com.around.aroundcore.web.enums.CoordsAPIType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoordsAPIFactoryTest {

    @Autowired
    CoordsAPI coordsAPI;

    @Value("${around.coordsapi}")
    private String coordsAPIType;

    @Test
    void testFactory(){
        CoordsAPIType type = CoordsAPIType.valueOf(coordsAPIType);
        Assertions.assertEquals(type.name(),coordsAPI.getType().name());
    }

}
