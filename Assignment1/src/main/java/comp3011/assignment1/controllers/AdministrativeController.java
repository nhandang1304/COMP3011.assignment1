package comp3011.assignment1.controllers;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import comp3011.assignment1.models.ServerUptimeResponse;

@RestController
@RequestMapping("/api/v1/admin")
public class AdministrativeController {
	private final Instant serverStartTime = Instant.now();
	@GetMapping("/uptime")
	public ServerUptimeResponse getServerUptime() {
		Instant currentTimeResponse = Instant.now();
		double serverUptimeSeconds = Duration.between(serverStartTime, currentTimeResponse).toMillis();
		return new ServerUptimeResponse(serverStartTime, currentTimeResponse, serverUptimeSeconds);
	}
}
