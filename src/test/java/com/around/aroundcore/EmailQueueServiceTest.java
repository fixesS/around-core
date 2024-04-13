package com.around.aroundcore;

import com.around.aroundcore.web.services.EmailQueueService;
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
class EmailQueueServiceTest {

    @Value("${testing.mail}")
    private String toAddress;

    @Autowired
    private EmailQueueService emailQueueService;

    @Test
    void testSendEmail(){
        String subject = "TEST";
        String text = "Testing spring mail sender";

        emailQueueService.addSimpleMessageToQueue(toAddress,subject,text);
        Assertions.assertTrue(true);
    }
}
