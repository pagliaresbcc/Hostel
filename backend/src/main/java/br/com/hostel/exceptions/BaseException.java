package br.com.hostel.exceptions;

import org.springframework.http.HttpStatus;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message;
	private HttpStatus httpStatus;
	
	public BaseException(String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
