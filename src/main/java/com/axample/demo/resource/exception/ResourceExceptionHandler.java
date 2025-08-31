package com.axample.demo.resource.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.services.exceptions.ObjectNotFoundException;

public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException ex, HttpServletRequest request){
		StandardError error = new StandardError(
			System.currentTimeMillis(),
			HttpStatus.NOT_FOUND.value(),
			"Object not found",
			ex.getMessage(),
			request.getRequestURI()
				);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		
	}
	

}
