package comp3011.assignment1.models;


import java.time.Instant;

/*This record stores the standard 
 information returned to the client when an error occurs*/
public record ErrorsResponse(
		Instant timestamp,
        int status,
        String error,
        String message,
        String path) {}
		