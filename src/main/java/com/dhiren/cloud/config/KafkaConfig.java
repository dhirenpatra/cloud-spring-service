package com.dhiren.cloud.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${library.topic.name}")
    private String topic;

    @Value("${library.topic.test.name}")
    private String testTopic;

    @Value("${library.topic.partitions}")
    private int partitions;

    @Value("${library.topic.replicas}")
    private int replicas;

    @Bean
    public NewTopic createNewTopic() {
        return TopicBuilder.name(topic).partitions(partitions).replicas(replicas).build();
    }

    @Bean
    public NewTopic createNewTestTopic() {
        return TopicBuilder.name(testTopic).partitions(partitions).replicas(replicas).build();
    }

}
