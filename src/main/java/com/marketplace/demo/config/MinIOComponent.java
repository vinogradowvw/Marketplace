package com.marketplace.demo.config;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Component
public class MinIOComponent {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Autowired
    public MinIOComponent(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void putObject(String objectName, InputStream inputStream) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, -1, 10485760)
                    .build());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public long getFileSize(String name){
        StatObjectArgs args = StatObjectArgs.builder().bucket(bucketName).object(name).build();
        try {
            StatObjectResponse stat = minioClient.statObject(args);
            return stat.size();
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public Optional<InputStream> getObject(String objectName) {
        try{
            InputStream stream = minioClient
                    .getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return Optional.ofNullable(stream);
        } catch (ErrorResponseException | InsufficientDataException |
                 InternalException | InvalidKeyException | InvalidResponseException |
                 IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void deleteObject(String objectName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();

            minioClient.removeObject(removeObjectArgs);
            //TODO: Logger here:
            //System.out.println("Object " + objectName + " deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
