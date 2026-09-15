package comp3011.assignment1.models;

/*This record stores the total number of 
input and output tokens used by the application*/
public record GlobalStatsResponse(
		long inputTokens, 
		long outputTokens) {}