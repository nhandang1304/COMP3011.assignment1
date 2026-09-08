package comp3011.assignment1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import comp3011.assignment1.services.*;

@RestController
public class STTController {
	private final STTService sttService;
	
	public STTController(STTService sttService) {
		this.sttService = sttService;
	}
	@PostMapping("/api/speech")
	public String speech(@RequestParam("audioRecord") MultipartFile audioFile) {
		System.out.println("Speech endpoint called!");
		return sttService.transcript(audioFile);
		
	}
};
