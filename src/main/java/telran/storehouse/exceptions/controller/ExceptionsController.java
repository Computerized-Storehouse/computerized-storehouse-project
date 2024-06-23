package telran.storehouse.exceptions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import telran.storehouse.exceptions.NotFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionsController {

	private ResponseEntity<String> returnResponse(String message, HttpStatus status) {
		log.error(message);
		return new ResponseEntity<String>(message, status);
	}

	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<String> notFoundHandler(NotFoundException e) {
		return returnResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ IllegalStateException.class, IllegalArgumentException.class })
	ResponseEntity<String> illegalStateHandler(RuntimeException e) {
		return returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
