package ru.shulenin.farmownerapi.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.shulenin.farmownerapi.dto.AbstractDto;
import ru.shulenin.farmownerapi.dto.WorkerSaveEditDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
//@PropertySource("classpath:kafka.properties")
public class KafkaProducerConfig {
    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.producer.id}")
    private String kafkaProducerId;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
        return props;
    }

//    @Bean
//    public ProducerFactory<Long, ? extends AbstractDto> producerStarshipFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//    @Bean
//    @Scope(value = "prototype")
//    public KafkaTemplate<Long, ? extends AbstractDto> kafkaTemplate() {
//        KafkaTemplate<Long, ? extends AbstractDto> template = new KafkaTemplate<>(producerStarshipFactory());
//        template.setMessageConverter(new StringJsonMessageConverter());
//        return template;
//    }
}
