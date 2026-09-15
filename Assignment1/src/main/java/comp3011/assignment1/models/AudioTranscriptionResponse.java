package comp3011.assignment1.models;
import com.fasterxml.jackson.annotation.JsonProperty;

/*This record demonstrates the response returned by the speech-to-text service*/
public record AudioTranscriptionResponse(
		String text,
		TokenUsage usage) {
	
	// Represents the input and output token usage of the transcription request
	public record TokenUsage(
			/*
			 * @JsonProperty maps the JSON field
			 *  to the Java variables.
			 */
			@JsonProperty("input_tokens")
			long inputTokens, 
			@JsonProperty("output_tokens")
			long outputTokens) {}
}
		


