package comp3011.assignment1.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import comp3011.assignment1.controllers.AdministrativeController;
import comp3011.assignment1.models.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class AdministrativeControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(AdministrativeControllerTest.class);
	private AdministrativeController adminController;
	private ConfigurableApplicationContext appContext;
	@BeforeEach
	void setup() {
		appContext = mock(ConfigurableApplicationContext.class);
		adminController = new AdministrativeController(appContext);
	}
	
	// This test verifies that the uptime response contains valid time values
    // and a non-negative server uptime
	@Test
	void testValidServerUptimeResponse() {
		ServerUptimeResponse uptimeResponse = adminController.getServerUptime();
		assertNotNull(uptimeResponse.utcServerStart());
		assertNotNull(uptimeResponse.utcNow());
		assertTrue(uptimeResponse.serverUptimeSeconds() >= 0);
		logger.info("Uptime response is valid (start: {}, now: {}, uptime: {} seconds)", uptimeResponse.utcServerStart(), uptimeResponse.utcNow(),uptimeResponse.serverUptimeSeconds());
       
	}
	
	// This test verifies that the first shutdown request returns HTTP 202
    // and the expected shutdown message
	@Test
	void testShutdownSuccessResponse() {
		ResponseEntity<?> shutdownResponse = adminController.shutdown();
		assertEquals(HttpStatus.ACCEPTED, shutdownResponse.getStatusCode());
        assertInstanceOf(ServerShutdownResponse.class, shutdownResponse.getBody());
        assertEquals("Graceful shutdown requested.", ((ServerShutdownResponse) shutdownResponse.getBody()).message());
        logger.info("Shutdown request returned HTTP {} with expected message", shutdownResponse.getStatusCode().value());
      
	}
	
	// This test verifies that a second shutdown request is rejected with HTTP 409
    // when a shutdown is already in progress.
	@Test 
	void testShutdownUnsuccessResponse() {
		adminController.shutdown();
		ResponseEntity<?> shutdownResponse = adminController.shutdown();
		assertEquals(HttpStatus.CONFLICT, shutdownResponse.getStatusCode());
		ErrorsResponse error = (ErrorsResponse) shutdownResponse.getBody();
        assertEquals(409, error.status());
        assertEquals("/api/v1/admin/shutdown", error.path());
	}
}
