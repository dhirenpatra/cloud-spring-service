package com.dhiren.cloud.scheduler;

import com.dhiren.cloud.entity.FailedRecord;
import com.dhiren.cloud.entity.LibraryEvent;
import com.dhiren.cloud.enums.RecordStatus;
import com.dhiren.cloud.repo.FailedRecordRepository;
import com.dhiren.cloud.service.PersistEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.function.Function;

@Configuration
@Slf4j
public class RetryScheduler {

    private FailedRecordRepository repository;
    private PersistEventService eventService;

    public RetryScheduler(FailedRecordRepository repository, PersistEventService eventService) {
        this.repository = repository;
        this.eventService = eventService;
    }

    Function<FailedRecord, ConsumerRecord<Integer, String>> mapper =
            failedRecord -> new ConsumerRecord<>(
                failedRecord.getTopic(),
                failedRecord.getPartition(),
                failedRecord.getOffset_value(),
                failedRecord.getKey(),
                failedRecord.getRecord()
            );

    @Scheduled(fixedRate = 10000)
    public void pullRecord() throws JsonProcessingException {
        repository.findAllByStatus(RecordStatus.RETRYABLE)
            .stream()
            .map(mapper)
            .forEach( consumerRecord -> {
                try {
                    eventService.processLibraryEventConsumerRecord(consumerRecord);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
