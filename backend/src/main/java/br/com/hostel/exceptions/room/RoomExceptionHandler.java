package br.com.hostel.exceptions.room;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomExceptionHandler {

	@ExceptionHandler(RoomException.class)
	protected ResponseEntity<Object> handlerBaseException(RoomException ex) {
		return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
	}
}
