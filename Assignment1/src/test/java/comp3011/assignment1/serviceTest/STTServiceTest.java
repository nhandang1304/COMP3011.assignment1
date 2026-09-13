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
	@Test
	void testTranscriptUpdatesGlobalStats() {
		AudioTranscriptionResponse.TokenUsage tokenUsage = new AudioTranscriptionResponse.TokenUsage(20, 8);
		AudioTranscriptionResponse response = new AudioTranscriptionResponse("Speech to text", tokenUsage);
		when(responseSpec.body(AudioTranscriptionResponse.class)).thenReturn(response);
        
        STTService service = new STTService(mockRestClient, mockStat);
        
        AudioTranscriptionResponse result = service.transcript(audio);

        assertEquals("Speech to text", result.text());
        verify(mockStat).updateTokens(20, 8);
	}
	@Test
	void testTranscriptThrowsWhenRestClientFails() {
		 when(responseSpec.body(AudioTranscriptionResponse.class))
         .thenThrow(new RuntimeException("Fail to reach OpenAI"));
	    STTService service = new STTService(mockRestClient, mockStat);
	    RuntimeException exception = assertThrows(RuntimeException.class,() -> service.transcript(audio));
	    assertEquals("Fail to reach OpenAI", exception.getMessage());	 
	    verify(mockStat, never()).updateTokens(anyLong(), anyLong());
	}
	
}
