package com.dhiren.cloud.entity;

import com.dhiren.cloud.enums.LibraryEventType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer libraryEventId;

    @Enumerated(EnumType.STRING)
    private LibraryEventType libraryEventType;

    @OneToOne(mappedBy = "libraryEvent", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Book book;

}
