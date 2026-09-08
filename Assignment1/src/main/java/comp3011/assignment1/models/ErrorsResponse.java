package comp3011.assignment1.models;

import java.time.Instant;
public record ErrorsResponse(
		Instant timestamp,
        int status,
        String error,
        String message,
        String path) {}
		