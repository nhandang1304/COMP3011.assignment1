package comp3011.assignment1.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import comp3011.assignment1.services.*;
import comp3011.assignment1.controllers.AdministrativeController;
import comp3011.assignment1.models.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class AdministrativeControllerTest {
	private AdministrativeController adminController;
	private ConfigurableApplicationContext appContext;
	@BeforeEach
	void setup() {
		appContext = mock(ConfigurableApplicationContext.class);
		adminController = new AdministrativeController(appContext);
	}
	@Test
	void testValidServerUptimeResponse() {
		ServerUptimeResponse uptimeResponse = adminController.getServerUptime();
		assertNotNull(uptimeResponse.utcServerStart());
		assertNotNull(uptimeResponse.utcNow());
		assertTrue(uptimeResponse.serverUptimeSeconds() >= 0);
	}
	@Test
	void testShutdownSuccessfulResponse() {
		ResponseEntity<?> shutdownResponse = adminController.shutdown();
		assertEquals(HttpStatus.ACCEPTED, shutdownResponse.getStatusCode());
        assertInstanceOf(ServerShutdownResponse.class, shutdownResponse.getBody());
        assertEquals("Graceful shutdown requested.", ((ServerShutdownResponse) shutdownResponse.getBody()).message());
		
	}
	@Test 
	void testShutdownUnsuccessfulResponse() {
		adminController.shutdown();
		ResponseEntity<?> shutdownResponse = adminController.shutdown();
		assertEquals(HttpStatus.CONFLICT, shutdownResponse.getStatusCode());
		ErrorsResponse error = (ErrorsResponse) shutdownResponse.getBody();
        assertEquals(409, error.status());
        assertEquals("/api/v1/admin/shutdown", error.path());
	}
}
