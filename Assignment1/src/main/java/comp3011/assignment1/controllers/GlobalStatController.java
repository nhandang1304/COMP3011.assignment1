package comp3011.assignment1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comp3011.assignment1.models.GlobalStatsResponse;
import comp3011.assignment1.services.GlobalStatService;

@RestController
@RequestMapping("/api/v1")
public class GlobalStatController {
	private final GlobalStatService globalStat;
	
	public GlobalStatController(GlobalStatService globalStat) {
		this.globalStat = globalStat;
	}
	@GetMapping("/global/stats")
	public GlobalStatsResponse getGlobalStat(GlobalStatService global) {
		long inputTokens = global.getInputTokens();
		long outputTokens = global.getOutputTokens();
		return new GlobalStatsResponse(inputTokens, outputTokens);
	}
}
