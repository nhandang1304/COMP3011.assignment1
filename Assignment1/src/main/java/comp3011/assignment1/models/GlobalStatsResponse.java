package comp3011.assignment1.models;
import com.fasterxml.jackson.annotation.JsonProperty;
public record GlobalStatsResponse(
		@JsonProperty("inputTokens")
		long inputTokens, 
		@JsonProperty("outputTokens")
		long outputTokens) {}