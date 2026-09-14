package comp3011.assignment1.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import comp3011.assignment1.controllers.STTController;
import comp3011.assignment1.models.*;
import comp3011.assignment1.services.STTService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class STTControllerTest {
	private STTService sttService;
	private STTController sttController;
	private MockMultipartFile audioFile;
	@BeforeEach
	void setUp() {
		sttService = mock(STTService.class);
		sttController = new STTController(sttService);
		audioFile = new MockMultipartFile("audioRecord", "test.wav", "audio/wav", "audioBytes".getBytes());
	}
	@Test
	void testSpeechReturnSusccessResponse() {
		AudioTranscriptionResponse.TokenUsage tokenUsage = new AudioTranscriptionResponse.TokenUsage(10, 5);
        AudioTranscriptionResponse successAudioTranscript = new AudioTranscriptionResponse("Speech to text successfully", tokenUsage);
        when(sttService.transcript(audioFile)).thenReturn(successAudioTranscript);
        
        ResponseEntity<?> response = sttController.speech(audioFile);
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals(successAudioTranscript, response.getBody());
        
	}
	@Test
	void testSpeechReturn500OnServiceFailure() {
		when(sttService.transcript(audioFile)).thenThrow(new RuntimeException("STT API unreachable"));
		 ResponseEntity<?> response = sttController.speech(audioFile);
	        
	        assertEquals(500, response.getStatusCode().value());
	        assertEquals("STT API unreachable", response.getBody());
	}
}
