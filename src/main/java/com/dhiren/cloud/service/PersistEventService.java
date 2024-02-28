package com.dhiren.cloud.service;

import com.dhiren.cloud.entity.LibraryEvent;
import com.dhiren.cloud.exceptions.custom.ValidationBusinessException;
import com.dhiren.cloud.repo.LibraryEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersistEventService {

    private final LibraryEventRepository repository;
    private final ObjectMapper objectMapper;

    public PersistEventService(LibraryEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public LibraryEvent processLibraryEventConsumerRecord(
            ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("Message Parsed Successfully with key {} value {} in partition {}",
                consumerRecord.key(), consumerRecord.value(), consumerRecord.partition());
        var event = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        switch (event.getLibraryEventType()) {
            case NEW, UPDATE -> {
                return save(event);
            }
            default -> throw new ValidationBusinessException("Unsupported Event Type Received");
        }
    }

    private LibraryEvent save(LibraryEvent event) {
        log.info("Record Received for Storing {} ",event);
        event.getBook().setLibraryEvent(event);
        var savedEntity = repository.save(event);
        log.info("Record Stored for Storing {} ",event);
        return savedEntity;
    }
}
