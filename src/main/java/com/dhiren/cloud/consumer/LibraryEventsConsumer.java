package com.dhiren.cloud.consumer;

import com.dhiren.cloud.service.PersistEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.dhiren.cloud.constants.AppConstants.TOPIC;

@Component
@Slf4j
public class LibraryEventsConsumer {

    private final PersistEventService eventService;

    public LibraryEventsConsumer(PersistEventService eventService) {
        this.eventService = eventService;
    }

    @KafkaListener(topics = {TOPIC})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("Message Received Successfully with key {} value {} in partition {}",
                consumerRecord.key(), consumerRecord.value(), consumerRecord.partition());
        eventService.processLibraryEventConsumerRecord(consumerRecord);
    }

}
