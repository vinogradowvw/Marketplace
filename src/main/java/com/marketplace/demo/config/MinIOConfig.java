package com.marketplace.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.minio.MinioClient;

@Configuration
public class MinIOConfig {

    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.port}")
    private Integer minioPort;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucket-name}")
    private String bucketName;
    @Value("${minio.secure}")
    private Boolean minioSecure;

    @Bean
    public MinioClient minioClient() {

        return MinioClient.builder().credentials(accessKey, secretKey)
                .endpoint(minioUrl, minioPort, minioSecure).build();
    }
}
