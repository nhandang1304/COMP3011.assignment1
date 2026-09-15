package comp3011.assignment1.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import comp3011.assignment1.services.*;
import comp3011.assignment1.models.AudioTranscriptionResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class STTServiceTest {
	private GlobalStatService mockStat;
	private RestClient mockRestClient;
	private MultipartFile audio;
    private RestClient.RequestBodyUriSpec requestSpec;
    private RestClient.ResponseSpec responseSpec;

	@BeforeEach
	void setUp() {
		mockStat = mock(GlobalStatService.class);
		mockRestClient = mock(RestClient.class);
		
		requestSpec = mock(RestClient.RequestBodyUriSpec.class, RETURNS_SELF);
        responseSpec = mock(RestClient.ResponseSpec.class);

        when(mockRestClient.post()).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        
		audio = new MockMultipartFile("audioRecord", "testAudio.wav", "audio/wav", "audioByte".getBytes());
	}
	
	// This test checks that a successful transcription returns the expected text
    // and updates the global token statistics with the API usage
	@Test
	void testTranscriptUpdatesGlobalStats() {
		AudioTranscriptionResponse.TokenUsage tokenUsage = new AudioTranscriptionResponse.TokenUsage(20, 8);
		AudioTranscriptionResponse response = new AudioTranscriptionResponse("Speech to text", tokenUsage);
		
		// Simulates a successful response from the STT API
		when(responseSpec.body(AudioTranscriptionResponse.class)).thenReturn(response);
        
        STTService service = new STTService(mockRestClient, mockStat);
        
        AudioTranscriptionResponse result = service.transcript(audio);
        
        assertEquals("Speech to text", result.text()); // Checks that the transcription text is returned correctly
        verify(mockStat).updateTokens(20, 8); // Checks that the returned token usage is added to global statistics
	}
	
	// This test checks that an STT API failure throw an exception
    // and does not update the global token statistics
	@Test
	void testTranscriptThrowsWhenRestClientFails() {
		
		// Simulates a failure when receiving the STT API response
		 when(responseSpec.body(AudioTranscriptionResponse.class))
         .thenThrow(new RuntimeException("Fail to reach OpenAI"));
	    STTService service = new STTService(mockRestClient, mockStat);
	    RuntimeException exception = assertThrows(RuntimeException.class,() -> service.transcript(audio));
	    assertEquals("Fail to reach OpenAI", exception.getMessage());	// Checks that the original error message is preserved
	    verify(mockStat, never()).updateTokens(anyLong(), anyLong()); // Checks that failed requests do not add token usage to the statistics
	}
	
}
