package com.dhiren.cloud.contoller;

import com.dhiren.cloud.model.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/libraryevent")
@Slf4j
public class LibraryEventsController {

    @PostMapping
    public ResponseEntity<LibraryEvent> postLibraryEvent(final @RequestBody LibraryEvent libraryEvent) {
        log.info("library event : {} ", libraryEvent);
        // TODO : Produce message to kafka here.
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
