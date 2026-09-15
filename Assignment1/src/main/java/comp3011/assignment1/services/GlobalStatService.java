package comp3011.assignment1.services;

import java.util.concurrent.atomic.LongAdder;
import org.springframework.stereotype.Service;

/*This service tracks the total input and output tokens used by 
the application and updates these values when multiple requests 
run concurrently.*/
@Service
public class GlobalStatService {
	private LongAdder totalInputTokens = new LongAdder();
	private LongAdder totalOutputTokens = new LongAdder();
	
	// This method adds token usage from a completed transcription request to the global totals
	public void updateTokens(long usedInputTokens, long usedOutputTokens) {
		totalInputTokens.add(usedInputTokens);
		totalOutputTokens.add(usedOutputTokens);
	}
	// This method returns the total number of input tokens used
	public long getInputTokens() { 
		return totalInputTokens.sum();
	}
	// This method returns the total number of output tokens used
	public long getOutputTokens() {
		return totalOutputTokens.sum();
	}
}
