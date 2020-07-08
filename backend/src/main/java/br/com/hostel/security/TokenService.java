package br.com.hostel.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.hostel.model.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forum.jwt.expiration}") //@value serve para pegar a propriedade do application.properties
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;
	
	public String generateToken(Authentication authentication) {
		Customer logado = (Customer) authentication.getPrincipal(); //pegando o usuario logado
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		return Jwts.builder()
				.setIssuer("API do Albergue") //quem fez a geração do token
				.setSubject(logado.getId().toString()) //usuario a quem esse token pertence
				.setIssuedAt(hoje) //data de geração
				.setExpiration(dataExpiracao) //data de expiração
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
		return Long.parseLong(claims.getSubject()); //no metodo gerarToken é setado Subject, que é usado nesse método de volta, para pegar o id
		
	}
}
