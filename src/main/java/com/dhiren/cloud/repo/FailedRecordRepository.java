package com.dhiren.cloud.repo;

import com.dhiren.cloud.entity.FailedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedRecordRepository extends JpaRepository<FailedRecord, Integer> {
}
