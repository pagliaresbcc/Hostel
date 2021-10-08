package br.com.hostel.tests.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class AuthenticationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	private LoginForm login = new LoginForm();
	
	@Test
	public void shouldAutenticateAndReturnStatusOK() throws JsonProcessingException, Exception {
		login.setEmail("maria@email.com");
		login.setPassword("123456");

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
	public void shouldNotAutenticateByWrongPasswordAndReturnStatusBadRequest() throws JsonProcessingException, Exception {
		login.setEmail("maria@email.com");
		login.setPassword("wrong password");
		
		mockMvc
			.perform(post("/auth")
			.content(objectMapper.writeValueAsString(login))
			.contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andReturn();	
	}
	
	@Test
	public void shouldNotAutenticateByNonexistentUserAndReturnStatusBadRequest() throws JsonProcessingException, Exception {
		login.setEmail("nonexistent@email.com");
		login.setPassword("wrong password");
		
		mockMvc
			.perform(post("/auth")
			.content(objectMapper.writeValueAsString(login))
			.contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andReturn();	
	}
}
