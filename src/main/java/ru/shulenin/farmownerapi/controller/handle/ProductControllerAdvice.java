package ru.shulenin.farmownerapi.controller.handle;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;

@RestControllerAdvice(basePackages = "ru.shulenin.farmownerapi.controller")
public class ProductControllerAdvice {

    @ExceptionHandler(ThereAreNotEntities.class)
    public String handle(Exception e) {
        return "ThereAreNotEntities";
    }
}
