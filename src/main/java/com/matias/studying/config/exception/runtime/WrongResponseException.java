package com.matias.studying.config.exception.runtime;

@SuppressWarnings("serial")
public class WrongResponseException extends RuntimeException{

	  public WrongResponseException(String message) {
	    super(message);
	  }
	  
	}
