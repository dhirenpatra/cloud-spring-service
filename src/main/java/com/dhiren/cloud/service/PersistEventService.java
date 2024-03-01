package com.dhiren.cloud.service;

import com.dhiren.cloud.entity.LibraryEvent;
import com.dhiren.cloud.exceptions.custom.LibraryEventNotFoundException;
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

    public void processLibraryEventConsumerRecord(
            ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("Message Parsed Successfully with key {} value {} in partition {}",
                consumerRecord.key(), consumerRecord.value(), consumerRecord.partition());
        var event = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        switch (event.getLibraryEventType()) {
            case NEW -> {
                save(event);
            }
            case UPDATE -> {
                validateUpdation(event);
                save(event);
            }
            default -> throw new ValidationBusinessException("Unsupported Event Type Received");
        }
    }

    private void save(LibraryEvent event) {
        log.info("Record Received for Storing {} ",event);
        event.getBook().setLibraryEvent(event);
        repository.save(event);
        log.info("Record Stored for Storing {} ",event);
    }

    private void validateUpdation(LibraryEvent event) {
        if(event.getLibraryEventId() == null) {
            throw new ValidationBusinessException("Event ID is Null.");
        }
        if(repository.findById(event.getLibraryEventId()).isEmpty()) {
            throw new LibraryEventNotFoundException("Book with provided id doesn't exist.");
        }
    }
}
