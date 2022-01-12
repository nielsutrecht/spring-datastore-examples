package com.nibado.example.datastores.sharedtests;

import org.testcontainers.utility.DockerImageName;

public class DockerImages {
    public static final DockerImageName REDIS = DockerImageName.parse("redis:6.2.6-alpine");
    public static final DockerImageName ELASTIC_SEARCH = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.12.0");
    public static final DockerImageName KAFKA = DockerImageName.parse("confluentinc/cp-kafka:7.0.0");
    public static final DockerImageName INFINISPAN = DockerImageName.parse("infinispan/server:13.0");
    public static final DockerImageName CASSANDRA = DockerImageName.parse("cassandra:3.11.10");
    public static final DockerImageName NEO4J = DockerImageName.parse("neo4j:4.4");
    public static final DockerImageName DYNAMODB = DockerImageName.parse("amazon/dynamodb-local:1.18.0");
}
