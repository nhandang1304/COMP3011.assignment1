package comp3011.assignment1.models;
import com.fasterxml.jackson.annotation.JsonProperty;



		
public record AudioTranscriptionResponse(
		String text,
		TokenUsage usage) {
	
	public record TokenUsage(
			@JsonProperty("input_tokens")
			long inputTokens, 
			@JsonProperty("output_tokens")
			long outputTokens) {}
}
		


