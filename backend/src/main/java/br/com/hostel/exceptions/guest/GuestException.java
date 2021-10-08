package br.com.hostel.exceptions.guest;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String message;
	private HttpStatus httpStatus;
}
