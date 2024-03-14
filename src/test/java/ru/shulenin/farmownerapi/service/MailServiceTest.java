package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class MailServiceTest {
    private final MailService mailService;

    @Test
    void send() throws Exception {
        mailService.send("ilya.shulenin36@gmail.com", "TEST",
                "templates/report-mail.html");
    }
}