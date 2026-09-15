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

/*This controller manages administrative operations including checking server uptime
and a graceful server shutdown.*/
@RestController
@RequestMapping("/api/v1")
public class AdministrativeController {
	private static Logger logger = LoggerFactory.getLogger(AdministrativeController.class);
	private final Instant serverStartTime = Instant.now();   // The time when the server starts
	private final ConfigurableApplicationContext appContext;
	private AtomicBoolean inShutdownProgress = new AtomicBoolean(false); // Only one shutdown request can run at a time.
	
	public AdministrativeController(ConfigurableApplicationContext appContext) {
		this.appContext = appContext;
	}
	
	// This method calculates and returns the server uptime.
	@GetMapping("/admin/uptime")
	public ServerUptimeResponse getServerUptime() {
		Instant currentTimeResponse = Instant.now();
		double serverUptimeSeconds = Duration.between(serverStartTime, currentTimeResponse).toMillis() / 1000.0;
		logger.info("Server uptime response (server start time: {}, current time response: {}, server uptime: {})", 
				serverStartTime, currentTimeResponse, serverUptimeSeconds);
		return new ServerUptimeResponse(serverStartTime, currentTimeResponse, serverUptimeSeconds);
	}
	
	// This method requests a graceful shutdown and rejects duplicate shutdown requests.
	@PostMapping("/admin/shutdown")
	public ResponseEntity<?> shutdown(){
		ServerShutdownResponse shutdownMessage = new ServerShutdownResponse("Graceful shutdown requested.");
		
		// Marks the shutdown as in progress
		boolean firstRequest = inShutdownProgress.compareAndSet(false, true);
		
		// If there are more than one request, system returns error
		if (!firstRequest) {
			ErrorsResponse errorResponse = new ErrorsResponse(Instant.now(), 
														HttpStatus.CONFLICT.value(), 
														HttpStatus.CONFLICT.getReasonPhrase(),
														"Graceful shutdown is already in progress.",
														"/api/v1/admin/shutdown");
			logger.error(errorResponse.message());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
		}
		
		// Run the shutdown in a separate thread so the HTTP request can quickly return a 202 Accepted response
		Runnable shutdownTask = ()-> {try {Thread.sleep(800);}	// Give server time to return the shutdown response								
									  catch (InterruptedException e) {e.printStackTrace(); logger.error(e.getMessage());}
									  appContext.close(); }; // Close the Spring application context and shuts down the server
		Thread thread = new Thread(shutdownTask); 
		
		thread.start();
		logger.info("Graceful shutdown initiated");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(shutdownMessage);
	}
}
