package com.dhiren.cloud.entity;

import com.dhiren.cloud.enums.RecordStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString
@Builder
@Entity
public class FailedRecord {

    @Id
    @GeneratedValue
    private Integer id;

    private String topic;
    private String record;
    private String exception;

    private Integer key;
    private Integer partition;
    private Long offset_value;

    @Enumerated(value = EnumType.STRING)
    private RecordStatus status;
}
