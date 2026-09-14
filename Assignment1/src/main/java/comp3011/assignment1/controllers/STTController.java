 package comp3011.assignment1.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import comp3011.assignment1.services.STTService;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@RestController
public class STTController {
	private final STTService sttService;
	public static Set<Boolean> virtualThreadSet = ConcurrentHashMap.newKeySet();
	private static Logger logger = LoggerFactory.getLogger(STTController.class);
	public STTController(STTService sttService) {
		this.sttService = sttService;
		
	}
	@PostMapping("/api/speech")
	public ResponseEntity<?> speech(@RequestParam("audioRecord") MultipartFile audioFile) {
		logger.info("Audio upload starts");	
		Thread currentThread = Thread.currentThread();

        logger.info("HTTP resquest thread: {}", currentThread);
        logger.info("Is Virtual Thread: {}", currentThread.isVirtual());

        virtualThreadSet.add(currentThread.isVirtual());
        
		try {
			return ResponseEntity.ok(sttService.transcript(audioFile));
		}		
		catch (Exception error) {
			return ResponseEntity.status(500).body(error.getMessage());
		}
		
	}
};
