package br.com.hostel.tests.initializer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.helper.Role;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GuestsInitializer {
	
	public static void initialize(HttpHeaders headers, Address address, Guest guest, MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
		
		LoginForm login = new LoginForm();

		//setting login variables to autenticate
		login.setEmail("admin@email.com");
		login.setPassword("123456");

		//posting on /auth to get token
		MvcResult resultAuth = mockMvc
				.perform(post("/auth")
				.content(objectMapper.writeValueAsString(login)).contentType("application/json"))
				.andReturn();	
			
		String contentAsString = resultAuth.getResponse().getContentAsString();

		LoginDto loginObjResponse = objectMapper.readValue(contentAsString, LoginDto.class);
		
		// seting header to put on post and delete request parameters
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + loginObjResponse.getToken());
		
		// setting address to put into the guest paramseters
		address.setAddressName("rua x");
		address.setCity("Amparo");
		address.setCountry("Brasil");
		address.setState("SP");
		address.setZipCode("13900-000");
		
		// setting guest
		guest.setAddress(address);
		guest.setBirthday(LocalDate.of(1900, 12, 12));
		guest.setEmail("washington2@orkut.com");
		guest.setName("Washington");
		guest.setLastName("Ferrolho");
		guest.setTitle("MRS.");
		guest.setPassword("1234567");
		guest.setRole(Role.ROLE_USER);
	}
}
