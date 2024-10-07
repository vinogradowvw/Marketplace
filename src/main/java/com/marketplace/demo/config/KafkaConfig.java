package com.marketplace.demo.config;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.topic.recSys-req}")
    private String recSysReqTopic;
    @Value("${kafka.topic.recSys-resp}")
    private String recSysRespTopic;
    @Value("${kafka.groupId.recSys}")
    private String recSysGroupId;

    @Bean(name="kafkaRecSysListenerConsumerFactory")
    ConcurrentKafkaListenerContainerFactory<String, JsonNode>
    kafkaListenerContainerFactory(@Qualifier("recSysConsumerFactory") ConsumerFactory<String, JsonNode> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, JsonNode> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, JsonNode> recSysConsumerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, recSysGroupId);

        JsonDeserializer<JsonNode> jsonDeserializer = new JsonDeserializer<>(JsonNode.class);
        jsonDeserializer.addTrustedPackages("*");

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, jsonDeserializer);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ProducerFactory<String, JsonNode> recSysProducerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(name="kafkaRecSysProducerTemplate")
    public KafkaTemplate<String, JsonNode> kafkaTemplate(@Qualifier("recSysProducerFactory") ProducerFactory<String, JsonNode> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
