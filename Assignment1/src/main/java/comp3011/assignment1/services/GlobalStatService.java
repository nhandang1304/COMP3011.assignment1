package comp3011.assignment1.services;

import org.springframework.stereotype.Service;

@Service
public class GlobalStatService {
	private final long totalInputTokens;
	private final long totalOutputTokens;
	
	public void updateTokens(long usedInputTokens, long usedOutputTokens) {
		totalInputTokens += usedInputTokens;
		totalOutputTokens += usedOutputTokens;
	}
}
