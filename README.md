# Spring Data Store Integration Test Examples

This project contains reference implementations demonstrating how to integrate with data stores and queues/topics and how to then
implement integration tests with them. I created this project mainly because there are often recurring patterns at my 
clients and I am able to reuse these approaches between them.

Most of the integration tests use the excellent [Testcontainers](https://www.testcontainers.org/) library to spin up docker
containers during tests.

The reference implementations implement [BaseDbIntegrationTest](./shared-tests/src/main/java/com/nibado/example/datastores/sharedtests/BaseDbIntegrationTest.java) 
in the case of data stores and/or [BaseTopicIntegrationTest](./shared-tests/src/main/java/com/nibado/example/datastores/sharedtests/BaseTopicIntegrationTest.java)
in the case of topics / queues. 

The [Hazelcast tests](./hazelcast/src/test/java/com/nibado/example/datastores/hazelcast) are a good example of how the 
Base tests are reused.

## Implemented

* [Spring Data JDBC](./spring-data-jdbc) - [Documentation](https://spring.io/projects/spring-data-jdbc)
* [Spring Data JPA](./spring-data-jpa) - [Documentation](https://spring.io/projects/spring-data-jpa)
* [HazelCast](./hazelcast) - [Documentation](https://hazelcast.com/blog/spring-boot/)
* [Kafka](./kafka) - [Documentation](https://spring.io/projects/spring-kafka)

## TODO

* Cassandra
* Neo4J
* ElasticSearch
* DynamoDB
* Redis