package com.dhiren.cloud.config;

import com.dhiren.cloud.enums.RecordStatus;
import com.dhiren.cloud.exceptions.custom.ValidationBusinessException;
import com.dhiren.cloud.service.FailedRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableKafka
@Slf4j
public class LibraryEventsConsumerConfig {

    @Value("${spring.kafka.producer.properties.retries}")
    private int retry;

    @Value("${library.retry.topic}")
    private String retryTopic;

    @Value("${library.retry.dlq}")
    private String dlqTopic;

    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private FailedRecordService failedRecordService;

    @Autowired
    public LibraryEventsConsumerConfig(KafkaTemplate<Integer, String> kafkaTemplate, FailedRecordService failedRecordService) {
        this.kafkaTemplate = kafkaTemplate;
        this.failedRecordService = failedRecordService;
    }

//    public DeadLetterPublishingRecoverer recoverer() {
//        return new DeadLetterPublishingRecoverer(kafkaTemplate,
//                (consumerRecord, exception) -> {
//            if(exception.getCause() instanceof ValidationBusinessException)
//                return new TopicPartition(dlqTopic, consumerRecord.partition());
//            else
//                return new TopicPartition(retryTopic, consumerRecord.partition());
//        });
//    }

    @SuppressWarnings(value = "unchecked cast")
    ConsumerRecordRecoverer recoverer = (consumerRecord, exception) -> {
      var record = (ConsumerRecord<Integer, String>) consumerRecord;
        if(exception.getCause() instanceof ValidationBusinessException) {
            failedRecordService.save(record, exception, RecordStatus.RETRYABLE);
        } else {
            failedRecordService.save(record, exception, RecordStatus.NON_RETRYABLE);
        }
    };


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

        var commonErrorHandler =
                new DefaultErrorHandler(
                        //recoverer(),
                        recoverer,
                        backoff()
                );

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
