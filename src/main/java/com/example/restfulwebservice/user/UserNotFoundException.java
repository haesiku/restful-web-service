package com.example.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// HTTP Status code
// 2XX --> OK
// 4XX --> Clint fault
// 5XX --> Server fault
@ResponseStatus(HttpStatus.NOT_FOUND) // Status: 404 Not Found
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String message) {
	
		super(message);
	}
}
