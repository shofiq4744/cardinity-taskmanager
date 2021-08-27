package com.cardinity.taskmanager.common;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestApiResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		
		Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
                );

        return RestApiResponse.ERROR.setResponse(errors).setMessage("Validation fail");
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestApiResponse onConstraintValidationException(ConstraintViolationException ex) {
		
		Map<String, String> errors = new HashMap<>();		
		ex.getConstraintViolations().forEach(error ->{
			errors.put(error.getPropertyPath().toString(), error.getMessage());
		});

		return RestApiResponse.ERROR.setResponse(errors).setMessage("Validation fail");
  }
	
}
