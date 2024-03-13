package ru.shulenin.farmownerapi.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class MailServiceTest {
    private final MailService mailService;
    private final ReportService reportService;

    @Test
    void send() throws Exception {
        mailService.send("ilya.shulenin36@gmail.com", "TEST",
                "templates/report-mail.html");
    }
}