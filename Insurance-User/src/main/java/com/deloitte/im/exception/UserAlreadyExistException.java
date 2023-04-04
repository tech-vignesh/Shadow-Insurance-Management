package com.deloitte.im.exception;

public class UserAlreadyExistException extends RuntimeException {



	private static final long serialVersionUID = 1L;

	public UserAlreadyExistException(String email) {
		super("User with Email  '"+email+"' already exists");
	}

	
	

}