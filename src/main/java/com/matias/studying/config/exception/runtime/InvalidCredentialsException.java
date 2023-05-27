package com.matias.studying.config.exception.runtime;

@SuppressWarnings("serial")
public class InvalidCredentialsException extends RuntimeException {

	public InvalidCredentialsException(String message) {
		super(message);
	}

}
