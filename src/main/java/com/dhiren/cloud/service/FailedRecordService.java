package com.dhiren.cloud.service;

import com.dhiren.cloud.entity.FailedRecord;
import com.dhiren.cloud.enums.RecordStatus;
import com.dhiren.cloud.repo.FailedRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@Slf4j
public class FailedRecordService {

    private FailedRecordRepository repository;

    public FailedRecordService(FailedRecordRepository repository) {
        this.repository = repository;
    }

    BiFunction<ConsumerRecord<Integer, String>, Throwable , FailedRecord> mapper = (consumerRecord , exception)
            -> FailedRecord.builder()
                            .id(null)
                            .topic(consumerRecord.topic())
                            .record(consumerRecord.value())
                            .key(consumerRecord.key())
                            .partition(consumerRecord.partition())
                            .offset_value(consumerRecord.offset())
                            .exception(exception.getMessage())
                            .build();

    public void save(ConsumerRecord<Integer, String> consumerRecord , Throwable t, RecordStatus status) {
        var mappedEntity = mapper.apply(consumerRecord, t);
        mappedEntity.setStatus(status);
        repository.save(mappedEntity);
    }
}
