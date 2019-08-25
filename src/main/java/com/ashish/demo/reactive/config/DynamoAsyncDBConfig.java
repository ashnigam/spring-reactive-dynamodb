package com.ashish.demo.reactive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.regions.Region;

import com.ashish.demo.reactive.MemoryUploadRepository;
import com.ashish.demo.reactive.UploadHandler;
import com.ashish.demo.reactive.UploadRepositoryInf;
import com.ashish.demo.reactive.dynamodb.DynamoDBUploadRepository;

@Configuration
@EnableWebFlux
public class DynamoAsyncDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String dBEndpoint;

    @Bean
    public DynamoDbAsyncClient dynamoDBAsyncClient() {
        return DynamoDbAsyncClient.builder().region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.builder().build()).build();
    }

    @Bean
    public UploadRepositoryInf uploadRepository() {
        return new DynamoDBUploadRepository(dynamoDBAsyncClient());
        // return new MemoryUploadRepository();
    }

    @Bean
    public UploadHandler uploadHandler() {
        return new UploadHandler(uploadRepository());
    }

}
