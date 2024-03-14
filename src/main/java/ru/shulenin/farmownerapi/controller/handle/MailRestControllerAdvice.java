package ru.shulenin.farmownerapi.controller.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice(basePackages = "ru.shulenin.farmownerapi.controller")
@Slf4j
public class MailRestControllerAdvice {
    
    @ExceptionHandler(MailException.class)
    public String handle(MailException e) {
        log.warn("Mail exception");
        log.warn(Arrays.toString(e.getStackTrace()));
        return "Mail exception";
    }
}
