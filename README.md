# Spring Data Store Integration Test Examples

This project contains reference implementations demonstrating how to integrate with data stores and queues/topics and how to then
implement integration tests with them. I created this project mainly because there are often recurring patterns at my 
clients and I am able to reuse these approaches between them.

Most of the integration tests use the excellent [Testcontainers](https://www.testcontainers.org/) library to spin up docker
containers during tests.

The reference implementations implement [BaseDbIntegrationTest](./shared-tests/src/main/java/com/nibado/example/datastores/sharedtests/BaseDbIntegrationTest.java) 
in the case of data stores, [BaseCacheIntegrationTest](./shared-tests/src/main/java/com/nibado/example/datastores/sharedtests/BaseCacheIntegrationTest.java)
in the case of caches and/or [BaseTopicIntegrationTest](./shared-tests/src/main/java/com/nibado/example/datastores/sharedtests/BaseTopicIntegrationTest.java)
in the case of topics / queues. This saves me a lot of time by not having to rewrite the same tests for all the different 
modules :)

The [Hazelcast tests](./hazelcast/src/test/java/com/nibado/example/datastores/hazelcast) are a good example of how the 
Base tests are reused.

## Implemented

* [JDBC](./jdbc) - [Documentation](https://spring.io/projects/spring-data-jdbc)
* [JPA](./jpa) - [Documentation](https://spring.io/projects/spring-data-jpa)
* [HazelCast](./hazelcast) - [Documentation](https://hazelcast.com/blog/spring-boot/)
* [Kafka](./kafka) - [Documentation](https://spring.io/projects/spring-kafka)
* [Cassandra](./cassandra) - [Documentation](https://docs.spring.io/spring-data/cassandra/docs/current/reference/html)
* [Redis](./redis) - [Documentation](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html)
* [ElasticSearch](./elasticsearch) - [Documentation](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html)
* [Infinispan](./infinispan) - [Documentation](https://infinispan.org/docs/dev/titles/spring_boot/starter.html)
* [Neo4J](./neo4j) - [Documentation](https://docs.spring.io/spring-data/neo4j/docs/current/reference/html)

## Notes

In general, it's important to remember that while most specific testcontainers (Cassandra, Postgres, etc.) expose the correct port by default,
the generic container doesn't do this. So if you need to use a GenericContainer, make sure you don't forget the `.withExposedPorts(<port>)`.

I personally prefer creating reusable context initializers over [@Container annotations](https://www.testcontainers.org/test_framework_integration/junit_5/) in my tests because it allows me to set any Spring config 
property before Spring itself starts. Another big benefit is that it allows me
to *not* use Testcontainers at all when I'm running on a CI system like Gitlab, and instead connect to a Gitlab service. 

But @Container annotations work just as well in most other cases.

### Infinispan

Infinispan was a bit more complex to set up than most others. A lot of documentation is for much older versions. In addition, the Infinispan documentation
forgets to mention that you still need to add the Spring Boot caching starter. You also have to programmatically create the caches. So if you 
see a message like this:

    Error received from the server: org.infinispan.server.hotrod.CacheNotFoundException: 
        Cache with name 'yourcache' not found amongst the configured caches

or:

    java.lang.IllegalArgumentException: Cannot find cache named 'yourcache' for Builder[..]

It's because you simply did not create one:

        var cfg = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.LOCAL)
                .build();

        cacheManager.administration().createCache("product", cfg);

Make sure you don't set CacheMode to something like DIST_SYNC because then it will try to connect a cluster, which it won't 
be able to, and then time out.

### Neo4J

The Neo4J 4.3 docker image seems to not listen properly on whatever Testcontainers is waiting on. 4.4 works fine :)

Also the [Spring Boot guide](https://spring.io/guides/gs/accessing-data-neo4j/) is unfortunately rather outdated.

## TODO

* DynamoDB