package comp3011.assignment1.controllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import comp3011.assignment1.controllers.GlobalStatController;
import comp3011.assignment1.services.GlobalStatService;
import comp3011.assignment1.models.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalStatControllerTest {
	 private static final Logger logger = LoggerFactory.getLogger(GlobalStatControllerTest.class);
	private GlobalStatService globalStat;
	private GlobalStatController globalController;
	
	@BeforeEach
	void setUp() {
		globalStat = mock(GlobalStatService.class);
		globalController = new GlobalStatController(globalStat);
	}
	
	// This test checks that the controller 
	// returns the correct input and output token counts
	@Test
	void testCorrectGlobalStatsResponse() {
		when(globalStat.getInputTokens()).thenReturn((long) 100);
		when(globalStat.getOutputTokens()).thenReturn((long) 50);
		GlobalStatsResponse statResponse = globalController.getGlobalStat();
		assertEquals(100, statResponse.inputTokens());
		assertEquals(50, statResponse.outputTokens());
		logger.info("Global statistics response is correct (input tokens: {}, output tokens: {})",statResponse.inputTokens(),statResponse.outputTokens());
        
	}
} 
