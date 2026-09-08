package comp3011.assignment1.services;

import org.springframework.stereotype.Service;

@Service
public class GlobalStatService {
	private long totalInputTokens;
	private long totalOutputTokens;
	
	public void updateTokens(long usedInputTokens, long usedOutputTokens) {
		totalInputTokens += usedInputTokens;
		totalOutputTokens += usedOutputTokens;
	}
	public long getInputTokens() {
		return totalInputTokens;
	}
	public long getOutputTokens() {
		return totalOutputTokens;
	}
}
