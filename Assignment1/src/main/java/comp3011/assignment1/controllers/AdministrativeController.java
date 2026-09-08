package comp3011.assignment1.controllers;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import comp3011.assignment1.models.ServerUptimeResponse;
import comp3011.assignment1.models.GlobalStatsResponse;
import comp3011.assignment1.models.ServerShutdownResponse;
import comp3011.assignment1.services.GlobalStatService;
@RestController
@RequestMapping("/api/v1")
public class AdministrativeController {
	private final Instant serverStartTime = Instant.now();
	
	@GetMapping("/admin/uptime")
	public ServerUptimeResponse getServerUptime() {
		Instant currentTimeResponse = Instant.now();
		double serverUptimeSeconds = Duration.between(serverStartTime, currentTimeResponse).toMillis() / 1000.0;
		return new ServerUptimeResponse(serverStartTime, currentTimeResponse, serverUptimeSeconds);
	}
	@GetMapping("/global/stats")
	public GlobalStatsResponse getGlobalStat(GlobalStatService global) {
		long inputTokens = global.getInputTokens();
		long outputTokens = global.getOutputTokens();
		return new GlobalStatsResponse(inputTokens, outputTokens);
	}
}
