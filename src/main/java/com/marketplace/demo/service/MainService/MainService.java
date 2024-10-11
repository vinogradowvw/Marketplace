package com.marketplace.demo.service.MainService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.service.kafkaService.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    private final KafkaSender kafkaSender;
    private final ObjectMapper objectMapper;

    @Autowired
    public MainService(KafkaSender kafkaSender, ObjectMapper objectMapper) {
        this.kafkaSender = kafkaSender;
        this.objectMapper = objectMapper;
    }

    public void sendUserData(UserDTO userDTO) {

        String key = "userId: " + userDTO.id();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("type", "user_service.init");
        rootNode.put("user_id", userDTO.id());

        kafkaSender.recSysSend(key, rootNode);
    }

}
