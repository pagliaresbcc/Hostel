package br.com.hostel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import br.com.hostel.repository.GuestRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticationService autenticationService;
	
	@Autowired
	private TokenService tokenService;

	@Autowired
	private GuestRepository guestRepository;

	// com esse método é possível importar o AuthenticationManager na classe
	// AutenticationController, que nao faz isso por padrao
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// configuracoes de autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//.passwordEncoder server para deixar a senha como hash, invés de salvá-la em texto aberto
		auth.userDetailsService(autenticationService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// configuracoes de autorizacao (url, quem pode acessar cada url, perfil de
	// acesso)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.antMatchers(HttpMethod.POST, "/auth").permitAll() // permite publicamente somente o post
				.antMatchers(HttpMethod.POST, "/api/guests").permitAll() 
				.anyRequest().authenticated() // qualquer outra requisicao precisara estar autenticado
////		.and().formLogin() //formulario de autenticacao nativo do spring (não usado pois ele cria sessão, deixando de ser stateless)
				.and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //diz para o spring nao criar session para cada cliente
				.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, guestRepository), UsernamePasswordAuthenticationFilter.class); //diz para o spring rodar o filtro criado antes de tudo
																					
		;
	}

	//configuracoes de recursos estaticos (requicoes pra arquivos css, js, imagens, etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
	}

	// new BCryptPasswordEncoder().encode gera uma senha no formato hash
//	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123456"));
//	}
}
