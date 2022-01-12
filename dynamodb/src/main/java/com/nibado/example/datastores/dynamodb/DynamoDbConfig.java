package com.nibado.example.datastores.dynamodb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Value("${dynamodb.endpoint}")
    private String dynamoDBEndpoint;

    @Bean
    public AwsCredentialsProvider credentials() {
        //Normally you'd use a proper AwsCredentialsProvider here. But we're only connecting to a local docker.
        return StaticCredentialsProvider.create(AwsBasicCredentials.create("ACCESS_KEY", "SECRET_KEY"));
    }

    @Bean
    public DynamoDbTable<ProductEntity> productTable(DynamoDbClient client, DynamoDbEnhancedClient enhancedClient) {
        var table = enhancedClient.table(ProductEntity.TABLE, TableSchema.fromBean(ProductEntity.class));

        if(!client.listTables().tableNames().contains(table.tableName())) {
            table.createTable();
        }

        return table;
    }

    @Bean
    public DynamoDbEnhancedClient enhancedClient(DynamoDbClient client) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient(AwsCredentialsProvider credentialsProvider, @Value("${dynamodb.region}") String region) {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamoDBEndpoint))
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
