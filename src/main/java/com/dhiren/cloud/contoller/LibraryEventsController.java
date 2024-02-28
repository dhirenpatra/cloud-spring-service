package com.dhiren.cloud.contoller;

import com.dhiren.cloud.model.LibraryEvent;
import com.dhiren.cloud.producer.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static com.dhiren.cloud.constants.AppConstants.PATH;

@RestController
@RequestMapping(PATH)
@Slf4j
public class LibraryEventsController {


    private final LibraryEventProducer libraryEventProducer;

    public LibraryEventsController(LibraryEventProducer libraryEventProducer) {
        this.libraryEventProducer = libraryEventProducer;
    }

    @PostMapping
    public ResponseEntity<LibraryEvent> postLibraryEvent(
            final @RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("library event : {} ", libraryEvent);
        libraryEventProducer.produceLibraryEvent(libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PostMapping("/app2")
    public ResponseEntity<LibraryEvent> postLibraryEventRecord(
            final @RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("library event : {} ", libraryEvent);
        libraryEventProducer.produceLibraryEventAsRecord(libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
