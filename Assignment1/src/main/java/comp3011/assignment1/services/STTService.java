package comp3011.assignment1.services;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import comp3011.assignment1.models.AudioTranscriptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/*This service class responsible for sending audio files to the speech-to-text API,
processing the transcription response, then update global token statistics.*/
@Service
public class STTService {
	private static Logger logger = LoggerFactory.getLogger(STTService.class);
	private final RestClient restClient;
	private final GlobalStatService globalStat;
	public STTService(RestClient restClient, GlobalStatService globalStat){
		this.restClient = restClient;
		this.globalStat = globalStat;
	}
	// This method sends audio file and returns the transcription response including text and token statistics.
	public AudioTranscriptionResponse transcript(MultipartFile file) {
		logger.info("Transcription request with fileSize: {} bytes", file.getSize());
		try {
			String apiKey = System.getenv("OPENAI_API_KEY");  // Retrieve the API key from the environment variable.
			MultiValueMap<String, Object> fileData = new LinkedMultiValueMap<>();  // Create the multipart form data required by the API.
			
			fileData.add("file", file.getResource());
			fileData.add("model", "gpt-4o-mini-transcribe");  // Specify the speech-to-text model
			
			// Send request to the API to transcribe into text
			AudioTranscriptionResponse response = restClient.post().uri("/v1/audio/transcriptions")
									.header("Authorization", "Bearer " + apiKey)
									.contentType(MediaType.MULTIPART_FORM_DATA)
									.body(fileData)
									.retrieve()
									.body(AudioTranscriptionResponse.class);
			
			// Add the token usage to the global statistics
			globalStat.updateTokens(response.usage().inputTokens(), response.usage().outputTokens());
			logger.info("Transcription successful (inputTokens: {}, outputTokens: {})",response.usage().inputTokens(), response.usage().outputTokens());
			return response;
		}
		catch(Exception error) {			
			logger.error("Transcription failed: {}", error.getMessage());
		    // Pass error back to controller
			throw error;
		}
		
	}
}
