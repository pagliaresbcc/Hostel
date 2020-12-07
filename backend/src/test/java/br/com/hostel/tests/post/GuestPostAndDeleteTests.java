package br.com.hostel.tests.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.GuestDto;
import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Role;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GuestPostAndDeleteTests {

	@Autowired
	GuestRepository guestRespository;

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	private static URI uri;
	private static HttpHeaders headers = new HttpHeaders();
	private static Address address = new Address();
	private static Guest guest = new Guest();
	
	@BeforeAll
	public static void beforeAll(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper)
			throws JsonProcessingException, Exception {
		uri = new URI("/api/guests/");
		
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

	@Test
	public void shouldReturnNotFoundStatusWhenDeletingByNonExistentGuestID() throws Exception {
		
		addressRepository.save(address);
		guestRespository.save(guest);
		
		mockMvc
		.perform(delete(uri + "0")
				.headers(headers))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andReturn();	
	}
	
	@Test
	public void shouldAutenticateAndDeleteOneGuestWithId2() throws Exception {
		
		addressRepository.save(address);
		guest = guestRespository.save(guest);

		mockMvc
			.perform(delete(uri + guest.getId().toString())
			.headers(headers))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();	
	}
	
	@Test
	public void shouldAutenticateAndCreateOneGuestAndReturnStatusCreated() throws Exception {

		MvcResult result = 
				mockMvc
					.perform(post(uri)
					.headers(headers)
					.content(objectMapper.writeValueAsString(guest)))
					.andDo(print())
					.andExpect(status().isCreated())
					.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		GuestDto guestObjResponse = objectMapper.readValue(contentAsString, GuestDto.class);

		assertEquals(guestObjResponse.getName(), "Washington");
		assertEquals(guestObjResponse.getAddress().getCity(), "Amparo");
	}

}