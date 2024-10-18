package com.marketplace.demo.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
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
@EnableKafka
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
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, JsonNode.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name="kafkaRecSysReplProducer")
    public ReplyingKafkaTemplate<String, JsonNode, JsonNode> replyingTemplate(
            @Qualifier("recSysProducerFactory") ProducerFactory<String, JsonNode> pf,
            @Qualifier("recSysRepliesContainer") ConcurrentMessageListenerContainer<String, JsonNode> repliesContainer) {

        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, JsonNode> recSysRepliesContainer(
            @Qualifier("kafkaRecSysListenerConsumerFactory")
            ConcurrentKafkaListenerContainerFactory<String, JsonNode> containerFactory) {

        ConcurrentMessageListenerContainer<String, JsonNode> repliesContainer =
                containerFactory.createContainer(recSysRespTopic);
        repliesContainer.getContainerProperties().setGroupId(recSysGroupId);
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

    @Bean
    public ProducerFactory<String, JsonNode> recSysProducerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(name="kafkaRecSysProducerTemplate")
    public KafkaTemplate<String, JsonNode> kafkaTemplate(@Qualifier("recSysProducerFactory") ProducerFactory<String, JsonNode> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
