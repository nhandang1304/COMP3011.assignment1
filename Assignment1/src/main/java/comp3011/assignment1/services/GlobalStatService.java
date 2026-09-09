package comp3011.assignment1.services;

import java.util.concurrent.atomic.LongAdder;
import org.springframework.stereotype.Service;

@Service
public class GlobalStatService {
	private LongAdder totalInputTokens = new LongAdder();
	private LongAdder totalOutputTokens = new LongAdder();
	
	public void updateTokens(long usedInputTokens, long usedOutputTokens) {
		totalInputTokens.add(usedInputTokens);
		totalOutputTokens.add(usedOutputTokens);
	}
	public long getInputTokens() {
		return totalInputTokens.sum();
	}
	public long getOutputTokens() {
		return totalOutputTokens.sum();
	}
}
