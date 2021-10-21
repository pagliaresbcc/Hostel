package br.com.hostel.exceptions.guest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GuestExceptionHandler {

	@ExceptionHandler(GuestException.class)
	protected ResponseEntity<Object> handleBaseException(GuestException ex) {
		return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
	}
}
