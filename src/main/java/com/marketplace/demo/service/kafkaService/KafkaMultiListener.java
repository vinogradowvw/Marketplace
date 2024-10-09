package com.marketplace.demo.service.kafkaService;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.support.KafkaHeaders;

@Component
public class KafkaMultiListener {

    @KafkaListener(topics = "${kafka.topic.recSys-req}", groupId = "${kafka.groupId.recSys}",
            containerFactory = "kafkaRecSysListenerConsumerFactory")
    public void recSysListen(@Header(KafkaHeaders.RECEIVED_KEY) String key, JsonNode message) {
        System.out.println("Received message: " + message);
        String username = message.get("username").asText();
        // Обработка полученного сообщения
    }

}
