package comp3011.assignment1.controllers;


import java.time.Duration;
import java.time.Instant;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import comp3011.assignment1.models.ServerUptimeResponse;
import comp3011.assignment1.models.GlobalStatsResponse;
import comp3011.assignment1.models.ServerShutdownResponse;
import comp3011.assignment1.services.GlobalStatService;

@RestController
@RequestMapping("/api/v1")
public class AdministrativeController {
	private final Instant serverStartTime = Instant.now();
	private final ConfigurableApplicationContext appContext;
	public AdministrativeController(ConfigurableApplicationContext appContext) {
		this.appContext = appContext;
	}
	
	@GetMapping("/admin/uptime")
	public ServerUptimeResponse getServerUptime() {
		Instant currentTimeResponse = Instant.now();
		double serverUptimeSeconds = Duration.between(serverStartTime, currentTimeResponse).toMillis() / 1000.0;
		return new ServerUptimeResponse(serverStartTime, currentTimeResponse, serverUptimeSeconds);
	}
	@PostMapping("/admin/shutdown")
	public ResponseEntity shutdown() throws InterruptedException {
		ServerShutdownResponse shutdownMessage = new ServerShutdownResponse("Graceful shutdown requested.");
		
		Runnable shutdownTask = ()-> appContext.close();
		Thread thread = new Thread(shutdownTask);
		thread.sleep(1000);
		thread.start();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(shutdownMessage);
	}
}
