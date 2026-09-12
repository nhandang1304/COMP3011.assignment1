package comp3011.assignment1.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import comp3011.assignment1.models.AudioTranscriptionResponse;
import comp3011.assignment1.services.STTService;


@RestController
public class STTController {
	private final STTService sttService;
	
	public STTController(STTService sttService) {
		this.sttService = sttService;
		
	}
	@PostMapping("/api/speech")
	public ResponseEntity<?> speech(@RequestParam("audioRecord") MultipartFile audioFile) {
		System.out.println("Speech endpoint called!");	
		try {
			return ResponseEntity.ok(sttService.transcript(audioFile));
		}
		catch (Exception error) {
			return ResponseEntity.status(500).body(error.getMessage());
		}
		
	}
};
