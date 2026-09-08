package comp3011.assignment1.services;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class STTService {
	private final RestClient restClient;
	private final GlobalStatService globalStat;
	public STTService(RestClient restClient, GlobalStatService globalStat){
		this.restClient = restClient;
		this.globalStat = globalStat;
	}
	public String transcript(MultipartFile file) {
		String apiKey = System.getenv("OPENAI_API_KEY");
		MultiValueMap<String, Object> fileData = new LinkedMultiValueMap<>();
		fileData.add("file", file.getResource());
		fileData.add("model", "gpt-4o-mini-transcribe");
		String response = restClient.post().uri("/v1/audio/transcriptions")
								.header("Authorization", "Bearer " + apiKey)
								.contentType(MediaType.MULTIPART_FORM_DATA)
								.body(fileData)
								.retrieve()
								.body(String.class);
	}
}
