package ru.shulenin.farmownerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@PropertySource({
        "classpath:kafka.properties"
})
public class FarmOwnerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmOwnerApiApplication.class, args);
    }

}
