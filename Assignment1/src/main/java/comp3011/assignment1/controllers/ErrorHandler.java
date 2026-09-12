package comp3011.assignment1.controllers;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import comp3011.assignment1.models.ErrorsResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorsResponse> handleCommonException(Exception error, HttpServletRequest request){
		ErrorsResponse errorResponse = new ErrorsResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), 
															HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
															"An unexpected server error occurred.",
															request.getRequestURI());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
