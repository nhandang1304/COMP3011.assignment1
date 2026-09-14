package comp3011.assignment1.controllers;


import java.time.Duration;
import java.time.Instant;



import org.springframework.web.bind.annotation.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import comp3011.assignment1.models.ServerUptimeResponse;
import comp3011.assignment1.models.ServerShutdownResponse;
import comp3011.assignment1.models.ErrorsResponse;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/v1")
public class AdministrativeController {
	private static Logger logger = LoggerFactory.getLogger(AdministrativeController.class);
	private final Instant serverStartTime = Instant.now();
	private final ConfigurableApplicationContext appContext;
	private AtomicBoolean inShutdownProgress = new AtomicBoolean(false);
	
	public AdministrativeController(ConfigurableApplicationContext appContext) {
		this.appContext = appContext;
	}
	
	@GetMapping("/admin/uptime")
	public ServerUptimeResponse getServerUptime() {
		Instant currentTimeResponse = Instant.now();
		double serverUptimeSeconds = Duration.between(serverStartTime, currentTimeResponse).toMillis() / 1000.0;
		logger.info("Server uptime response (server start time: {serverStartTime}, current time response: {currentTimeResponse}, server uptime: {serverUptimeSeconds})");
		return new ServerUptimeResponse(serverStartTime, currentTimeResponse, serverUptimeSeconds);
	}
	@PostMapping("/admin/shutdown")
	public ResponseEntity<?> shutdown(){
		ServerShutdownResponse shutdownMessage = new ServerShutdownResponse("Graceful shutdown requested.");
		
		boolean firstRequest = inShutdownProgress.compareAndSet(false, true);
		if (!firstRequest) {
			ErrorsResponse errorResponse = new ErrorsResponse(Instant.now(), 
														HttpStatus.CONFLICT.value(), 
														HttpStatus.CONFLICT.getReasonPhrase(),
														"Graceful shutdown is already in progress.",
														"/api/v1/admin/shutdown");
			logger.error(errorResponse.message());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
		}
		
		Runnable shutdownTask = ()-> {try {Thread.sleep(800);}										
									  catch (InterruptedException e) {e.printStackTrace(); logger.error(e.getMessage());}
									  appContext.close(); };
		Thread thread = new Thread(shutdownTask);
		
		thread.start();
		logger.info("Successfully shutdown");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(shutdownMessage);
	}
}
