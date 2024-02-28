package com.dhiren.cloud.model;

import com.dhiren.cloud.enums.LibraryEventType;
import jakarta.validation.constraints.NotNull;

public record LibraryEvent (
        @NotNull(message = "libraryEventId can't be null or empty. Please provide an id") Integer libraryEventId,
        LibraryEventType libraryEventType,
        Book book) {
}
