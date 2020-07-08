package br.com.hostel.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.form.LoginForm;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class AutenticationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	private LoginForm login = new LoginForm();
	
	@Test
	public void shouldAutenticateAndReturnStatusOK() throws JsonProcessingException, Exception {
		//setting login variables to autenticate
		login.setEmail("aluno@email.com");
		login.setPassword("123456");

		//posting on /auth to get token
		MvcResult resultAuth = mockMvc
				.perform(post("/auth")
				.content(objectMapper.writeValueAsString(login))
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();	
			
		String contentAsString = resultAuth.getResponse().getContentAsString();
		LoginDto loginObjResponse = objectMapper.readValue(contentAsString, LoginDto.class);

		assertNotNull(loginObjResponse);
		assertEquals(loginObjResponse.getType(), "Bearer");
	}
	
	@Test
	public void shouldNotAutenticateAndReturnStatusBadRequest() throws JsonProcessingException, Exception {
		//setting login variables to autenticate
		login.setEmail("aluno@email.com");
		login.setPassword("11111");
		
		//posting on /auth to get token
		mockMvc
			.perform(post("/auth")
					.content(objectMapper.writeValueAsString(login))
					.contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andReturn();	
	}
}
