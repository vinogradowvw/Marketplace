package com.marketplace.demo.service.kafkaService;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {

    private final KafkaTemplate<String, JsonNode> recSysTemplate;
    @Value("kafka.topic.recSys-req")
    private String topic;

    @Autowired
    public KafkaSender(@Qualifier("kafkaRecSysProducerTemplate") KafkaTemplate<String, JsonNode> recSysTemplate){
        this.recSysTemplate = recSysTemplate;
    }

    public void recSysSend(String key, JsonNode toSend) {
        recSysTemplate.send(topic, key, toSend);
    }

}
