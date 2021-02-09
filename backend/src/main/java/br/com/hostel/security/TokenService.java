package br.com.hostel.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.hostel.model.Guest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forum.jwt.expiration}") 
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;
	
	public String generateToken(Authentication authentication) {
		
		Guest loggedUser = (Guest) authentication.getPrincipal();
		Date todayDate = new Date();
		Date expirationDate = new Date(todayDate.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("API do Albergue") //quem fez a geração do token
				.setSubject(loggedUser.getId().toString()) //usuario a quem esse token pertence
				.setIssuedAt(todayDate) //data de geração
				.setExpiration(expirationDate) //data de expiração
				.signWith(SignatureAlgorithm.HS256, secret) //usar a senha do application.properties / algoritmo de criptografia
				.compact();
	}

	public boolean isTokenValido(String token) {
		//parser() descriptografa o token e verifica se está ok
		try {
			
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getIdUsuario(String token) {
		
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		
		return Long.parseLong(claims.getSubject()); 
		
	}
}
