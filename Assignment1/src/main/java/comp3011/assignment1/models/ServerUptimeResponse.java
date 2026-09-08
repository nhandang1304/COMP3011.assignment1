package comp3011.assignment1.models;

import java.time.Instant;

public record ServerUptimeResponse(
		Instant utcServerStart,
        Instant  utcNow,
        double serverUptimeSeconds
		) {}