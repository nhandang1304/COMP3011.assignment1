package comp3011.assignment1.controllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import comp3011.assignment1.controllers.AdministrativeController;
import comp3011.assignment1.controllers.GlobalStatController;
import comp3011.assignment1.services.GlobalStatService;
import comp3011.assignment1.models.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class GlobalStatControllerTest {
	private GlobalStatService globalStat;
	private GlobalStatController globalController;
	
	@BeforeEach
	void setUp() {
		globalStat = mock(GlobalStatService.class);
		globalController = new GlobalStatController(globalStat);
	}
	@Test
	void testCorrectGlobalStatsResponse() {
		when(globalStat.getInputTokens()).thenReturn((long) 100);
		when(globalStat.getOutputTokens()).thenReturn((long) 50);
		GlobalStatsResponse statResponse = globalController.getGlobalStat();
		assertEquals(100, statResponse.inputTokens());
		assertEquals(50, statResponse.outputTokens());
	}
} 
