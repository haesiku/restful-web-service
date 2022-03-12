package com.example.restfulwebservice.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.restfulwebservice.user.UserNotFoundException;

@RestController
@ControllerAdvice // 모든 Controller 예외가 여기서 잡힘?
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundExceptions(Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	// 유효성검사 결과 출력위해서 handleMethodArgumentNotValid override 
	@Override // 명시적으로 작성하자, 없으면 method 이름 잘못되도 알수 없으니 @Override 표시하는게 좋
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(
				new Date(),
				"Validation Failed", //ex.getMessage(),
				ex.getBindingResult().toString());
		
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	
}
