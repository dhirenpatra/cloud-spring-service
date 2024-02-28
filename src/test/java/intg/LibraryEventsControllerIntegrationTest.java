package intg;

import com.dhiren.cloud.CloudSpringServiceApplication;
import com.dhiren.cloud.constants.AppConstants;
import com.dhiren.cloud.model.LibraryEvent;
import intg.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {CloudSpringServiceApplication.class})
@EmbeddedKafka(topics = "library-events")
@TestPropertySource(properties = {
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
class LibraryEventsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void postLibraryEvent() {
        LibraryEvent libraryEvent = TestUtil.libraryEventRecord();

        var httpHeader = new HttpHeaders();
        var httpEntity = new HttpEntity<>(libraryEvent, httpHeader);
        httpHeader.set("Content-type", MediaType.APPLICATION_JSON.toString());
        ResponseEntity<LibraryEvent> responseEntity = testRestTemplate.exchange(AppConstants.PATH, HttpMethod.POST, httpEntity, LibraryEvent.class);

        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
    }
}