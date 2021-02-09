package br.com.hostel.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.model.Guest;
import br.com.hostel.security.TokenService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins ="*", allowedHeaders = "*")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<LoginDto> autenticate(@RequestBody @Valid LoginForm form){
		
		UsernamePasswordAuthenticationToken loginData = form.convert();
		
		try {
			
			Authentication authentication = authManager.authenticate(loginData);
			
			String token = tokenService.generateToken(authentication); 
			
			Guest loggedGuest = (Guest) authentication.getPrincipal();
			
			return ResponseEntity.ok(new LoginDto(token, "Bearer", loggedGuest.getId(), loggedGuest.getRole()));
		} catch (AuthenticationException e) { 
			return ResponseEntity.badRequest().build();
		}
	}
}
