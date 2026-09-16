package comp3011.assignment1.serviceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;
import comp3011.assignment1.services.GlobalStatService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.Runnable;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Future;

public class GlobalStatServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(GlobalStatServiceTest.class);
	private GlobalStatService globalStat;
	@BeforeEach
	void setUp() {
		globalStat = new GlobalStatService();
	}
	
	// This test checks that the initial input and output token counts are both zero
	@Test
	void testFirstTokensEqualZero() {
		assertEquals(0, globalStat.getInputTokens());
		assertEquals(0, globalStat.getOutputTokens());
		logger.info("Initial input and output token counts are both zero");
	}
	// This test checks that multiple token updates are correctly added
    // to the global input and output token totals
	@Test
	void testupdateTokens() {
		globalStat.updateTokens(50, 60);
		globalStat.updateTokens(70, 90);
		assertEquals(120, globalStat.getInputTokens());
		assertEquals(150, globalStat.getOutputTokens());
		logger.info("Token updates are correct (input tokens: {}, output tokens: {})",globalStat.getInputTokens(),globalStat.getOutputTokens());
        
	}
	
	// This test checks that token updates remain correct when 200 tasks
    // update the global statistics concurrently
	@Test
	void testConcurrentUpdates()throws Exception {
		int totalTasks = 200;
		ExecutorService executor = Executors.newFixedThreadPool(200); // Thread pool to execute the update tasks concurrently
	
		List<Future<?>> tasks = new ArrayList<>();
		
		// Create 200 tasks, with each task adding one input and output token
		for (int i = 0; i < totalTasks; i++) {
	       Runnable task = new Runnable() {
	       @Override
	       public void run() {
	    	   globalStat.updateTokens(1, 1);
	       }
	       };
	       Future<?> future = executor.submit(task);

	       tasks.add(future);

		}
		 // Waits for all concurrent tasks to finish before checking the totals
	    for (Future<?> smallTask : tasks) {
	        smallTask.get();
	    }

	    executor.shutdown();

	    
	    assertEquals(totalTasks, globalStat.getInputTokens());
	    assertEquals(totalTasks, globalStat.getOutputTokens());
	    logger.info("Concurrent token updates " + "(tasks: {}, input tokens: {}, output tokens: {})",totalTasks,globalStat.getInputTokens(),globalStat.getOutputTokens());
      
	}
	
	
	
}
