package comp3011.assignment1.models;

import java.time.Instant;

/*This record stores the server start time, 
current UTC time, and the server's uptime in seconds*/
public record ServerUptimeResponse(
		Instant utcServerStart,
        Instant  utcNow,
        double serverUptimeSeconds
		) {}