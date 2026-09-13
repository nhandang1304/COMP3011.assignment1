package comp3011.assignment1.serviceTest;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import comp3011.assignment1.services.*;
import comp3011.assignment1.models.AudioTranscriptionResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

public class STTServiceTest {

	@Test
	void testTranscriptUpdatesGlobalStats() {
		GlobalStatService mockStat = mock(GlobalStatService.class);
		RestClient mockRestClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
		AudioTranscriptionResponse.TokenUsage tokenUsage = new AudioTranscriptionResponse.TokenUsage(20, 8);
		AudioTranscriptionResponse response = new AudioTranscriptionResponse("Speech to text", tokenUsage);
		
        when(mockRestClient.post()
                .uri(anyString())
                .header(anyString(), anyString())
                .contentType(any())
                .body(any())
                .retrieve()
                .body(AudioTranscriptionResponse.class))
                .thenReturn(response);
        
        STTService service = new STTService(mockRestClient, mockStat);
        MultipartFile audio = new MockMultipartFile(
                "audioRecord", "testAudio.wav", "audio/wav", "audioByte".getBytes());
        AudioTranscriptionResponse result = service.transcript(audio);

        assertEquals("Speech to text", result.text());
        verify(mockStat).updateTokens(20, 8);
	}

	
}
