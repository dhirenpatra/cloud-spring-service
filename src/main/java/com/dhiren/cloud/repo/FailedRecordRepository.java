package com.dhiren.cloud.repo;

import com.dhiren.cloud.entity.FailedRecord;
import com.dhiren.cloud.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedRecordRepository extends JpaRepository<FailedRecord, Integer> {
    List<FailedRecord> findAllByStatus(RecordStatus recordStatus);
}
