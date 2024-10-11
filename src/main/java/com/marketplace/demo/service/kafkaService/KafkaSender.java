package com.marketplace.demo.service.kafkaService;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class KafkaSender {

    private final KafkaTemplate<String, JsonNode> recSysTemplate;
    private final ReplyingKafkaTemplate<String, JsonNode, JsonNode> replyingKafkaTemplate;
    @Value("kafka.topic.recSys-req")
    private String topic;

    @Autowired
    public KafkaSender(@Qualifier("kafkaRecSysProducerTemplate") KafkaTemplate<String, JsonNode> recSysTemplate,
                       @Qualifier("kafkaRecSysReplProducer") ReplyingKafkaTemplate<String, JsonNode, JsonNode> replyingKafkaTemplate) {
        this.recSysTemplate = recSysTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public void recSysSend(String key, JsonNode toSend) {
        recSysTemplate.send(topic, key, toSend);
    }

    public Optional<JsonNode> sendRecSysRequest(String key, JsonNode toSend) {

        ProducerRecord<String, JsonNode> record = new ProducerRecord<>(topic, key, toSend);
        RequestReplyFuture<String, JsonNode, JsonNode> replyFuture = replyingKafkaTemplate.sendAndReceive(record);

        Optional<JsonNode> response = Optional.empty();

        try {
            SendResult<String, JsonNode> sendResult = replyFuture.getSendFuture().get(3, TimeUnit.SECONDS);

            log.info("Message was sent: {}", sendResult.getRecordMetadata());

            ConsumerRecord<String, JsonNode> consumerRecord = replyFuture.get(4, TimeUnit.SECONDS);
            response = Optional.ofNullable(consumerRecord.value());
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getMessage());
        }

        return response;
    }

}
