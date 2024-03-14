package ru.shulenin.farmownerapi.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.data.convert.Jsr310Converters.StringToLocalDateConverter.INSTANCE;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(INSTANCE);
    }
}
