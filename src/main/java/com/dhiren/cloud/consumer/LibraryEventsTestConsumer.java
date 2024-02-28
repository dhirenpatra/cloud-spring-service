package com.dhiren.cloud.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.dhiren.cloud.constants.AppConstants.TOPIC_TEST;

@Component
@Slf4j
public class LibraryEventsTestConsumer implements AcknowledgingMessageListener<Integer,String> {


    @Override
    @KafkaListener(topics = {TOPIC_TEST})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Message Received Successfully with key {} value {} in partition {}",
                consumerRecord.key(), consumerRecord.value(), consumerRecord.partition());
        assert acknowledgment != null;
        acknowledgment.acknowledge();
    }

}
