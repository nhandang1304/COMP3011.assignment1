package comp3011.assignment1.regressionTest;

import comp3011.assignment1.controllers.STTController;
import comp3011.assignment1.models.AudioTranscriptionResponse;
import comp3011.assignment1.services.STTService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class STTControllerConcurrencyTest {
	Logger logger = LoggerFactory.getLogger(STTControllerConcurrencyTest.class);
    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private STTService sttService;

    private int totalRequest = 200;

    @BeforeEach
    void setUp() {
        STTController.virtualThreadSet.clear();
        
        AudioTranscriptionResponse mockResponse = new AudioTranscriptionResponse(
                "Success transcription", 
                new AudioTranscriptionResponse.TokenUsage(10, 5)
        );
        
        when(sttService.transcript(any(MultipartFile.class))).thenAnswer(invocation -> {
            Thread.sleep(100);
            return mockResponse;
        });
    }

    
    private HttpEntity<MultiValueMap<String, Object>> createRequestEntity() {
        ByteArrayResource audio = new ByteArrayResource("fake audio data".getBytes()) {
            @Override
            public String getFilename() {
                return "test.webm";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("audioRecord", audio);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new HttpEntity<>(body, headers);
    }

   
    private List<ResponseEntity<String>> sendConcurrentRequests() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(totalRequest);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = createRequestEntity();
        
        List<Callable<ResponseEntity<String>>> tasks = new ArrayList<>();
        for (int i = 0; i < totalRequest; i++) {
            tasks.add(() -> restTemplate.postForEntity("/api/speech", requestEntity, String.class));
        }

     
        List<Future<ResponseEntity<String>>> futures = executor.invokeAll(tasks);
        executor.shutdown();

        List<ResponseEntity<String>> responses = new ArrayList<>();
        for (Future<ResponseEntity<String>> future : futures) {
            responses.add(future.get(10, TimeUnit.SECONDS));
        }

        return responses;
    }

    @Test
    void shouldHandle200ConcurrentRequestsSuccessfully() throws Exception {
        long startTime = System.currentTimeMillis();
        List<ResponseEntity<String>> responses = sendConcurrentRequests();
        long duration = System.currentTimeMillis() - startTime;

        for (ResponseEntity<String> response : responses) {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("Success transcription"));
        } 

        logger.info("Total: %d and Execution time: %d ms%n", totalRequest, duration);
        assertEquals(totalRequest, responses.size());
    }

    @Test
    void testHandleAllRequestsUsingVirtualThreads() throws Exception {
        sendConcurrentRequests();

        assertFalse(STTController.virtualThreadSet.isEmpty(),
                "No requests were recorded in STTController");

        assertEquals(Set.of(true), STTController.virtualThreadSet,
                "Some requests were not processed by Virtual Threads");
    }
}