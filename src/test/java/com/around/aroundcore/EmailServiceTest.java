package com.around.aroundcore;

import com.around.aroundcore.web.services.EmailService;
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
class EmailServiceTest {

    @Value("${testing.mail}")
    private String toAddress;

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail(){
        String subject = "TEST";
        String text = "Testing spring mail sender";

        emailService.sendMessage(toAddress,subject,text);
        Assertions.assertTrue(true);
    }
}
