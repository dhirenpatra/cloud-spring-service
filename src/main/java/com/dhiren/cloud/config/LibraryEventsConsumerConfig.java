package com.dhiren.cloud.config;

import com.dhiren.cloud.exceptions.custom.ValidationBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableKafka
@Slf4j
public class LibraryEventsConsumerConfig {

    @Value("${spring.kafka.producer.properties.retries}")
    private int retry;

    @Bean
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        Objects.requireNonNull(factory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setConcurrency(3);
        factory.setCommonErrorHandler(commonErrorHandler());
        return factory;
    }

    private CommonErrorHandler commonErrorHandler() {
        // Exceptions to ignore
        var exceptionToIgnore = List.of(ValidationBusinessException.class);

        var commonErrorHandler = new DefaultErrorHandler(backoff());

        commonErrorHandler.setRetryListeners(((record, ex, deliveryAttempt) -> {
            log.error("Error Occurred while Consuming, Exception is {}, delivery attempts {} ",
                    ex.getMessage(), deliveryAttempt);
        }));

        // Add exceptions that to be ignored for retrying. We add those scenarios which will never
        // come to valid state.
        exceptionToIgnore.forEach(commonErrorHandler::addNotRetryableExceptions);
        return commonErrorHandler;
    }

    private BackOff backoff() {
        var backoff = new ExponentialBackOff();
        backoff.setInitialInterval(1500);
        backoff.setMultiplier(2);
        backoff.setMaxAttempts(retry);
        backoff.setMaxInterval(3000);
        return backoff;
    }

}
