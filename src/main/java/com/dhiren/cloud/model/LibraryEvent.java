package com.dhiren.cloud.model;

import com.dhiren.cloud.enums.LibraryEventType;

public record LibraryEvent (
        Integer libraryEventId,
        LibraryEventType libraryEventType,
        Book book) {
}
