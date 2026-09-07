package speechtotext.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class STTController {
	@PostMapping("/api/speech")
	public String speech(@RequestParam("audioRecord") MultipartFile file) {
		return "Hello";
	}
};
