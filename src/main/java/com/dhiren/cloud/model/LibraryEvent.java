package com.dhiren.cloud.model;

import com.dhiren.cloud.enums.LibraryEventType;
import jakarta.validation.constraints.NotNull;

public record LibraryEvent (
        @NotNull Integer libraryEventId,
        LibraryEventType libraryEventType,
        Book book) {
}
