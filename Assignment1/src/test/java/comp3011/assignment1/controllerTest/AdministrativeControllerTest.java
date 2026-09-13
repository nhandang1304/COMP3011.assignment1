package comp3011.assignment1.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import comp3011.assignment1.services.*;
import comp3011.assignment1.controllers.AdministrativeController;
import comp3011.assignment1.models.ServerUptimeResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	void testValidServerUptimeResponse() {
		ServerUptimeResponse response = adminController.getServerUptime();
		assertNotNull(response.utcServerStart());
		assertNotNull(response.utcNow());
		assertTrue(response.serverUptimeSeconds() >= 0);
	}
	void testValidShutdownResponse() {
		
	}
}
