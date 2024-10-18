package com.marketplace.demo.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.demo.controller.PostController;
import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.ReviewService.ReviewService;
import com.marketplace.demo.service.TagService.TagService;
import com.marketplace.demo.service.UserService.UserService;
import com.marketplace.demo.service.kafkaService.KafkaSender;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class PostControllerIntegrationTest {

    @Value("${kafka.topic.recSys-req}")
    private String recSysReqTopic;
    @Value("${kafka.topic.recSys-resp}")
    private String recSysRespTopic;
    @MockBean
    private PostService postService;
    @MockBean
    private DTOConverter<PostDTO, Post> postConverter;
    @MockBean
    private ImageService imageService;
    @MockBean
    private ProductService productService;
    @MockBean
    private UserService userService;
    @MockBean
    private TagService tagService;
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private PostController postController;

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1").asCompatibleSubstituteFor("apache/kafka"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeAll
    static void setUp() {
        kafka.start();
        createKafkaTopics();
    }

    private static void createKafkaTopics() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafka.getBootstrapServers());
        try (AdminClient adminClient = AdminClient.create(props)) {
            adminClient.createTopics(Collections.singletonList(new NewTopic("requests-for-recsys", 1, (short) 1)));
            adminClient.createTopics(Collections.singletonList(new NewTopic("recommendation-responses", 1, (short) 1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Consumer<String, String> createConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(props);
    }

    @Test
    public void sendToKafkaPost(){

        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setName("Test Post");

        Mockito.when(postService.readById(postId)).thenReturn(Optional.of(post));

        postController.sendPostToRecSys(postId);

        Consumer<String, String> consumer = createConsumer();
        consumer.subscribe(Collections.singletonList(recSysReqTopic));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
        consumer.close();

        assertFalse(records.isEmpty(), "Expected at least one record");
    }

}
