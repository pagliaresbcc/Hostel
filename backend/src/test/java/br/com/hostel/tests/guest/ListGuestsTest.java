package br.com.hostel.tests.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ListGuestsTest {

	@Autowired 
	private MockMvc mockMvc;
	
	@Autowired 
	private ObjectMapper objectMapper;

	private static URI uri;
	private static HttpHeaders headers = new HttpHeaders();
	private static Guest guest = new Guest();
	
	@BeforeAll
	public static void init(@Autowired GuestRepository guestRespository, 
			@Autowired AddressRepository addressRepository, @Autowired MockMvc mockMvc, 
			@Autowired ObjectMapper objectMapper) throws JsonProcessingException, Exception {
		
		Address address = new Address();
		LoginForm login = new LoginForm();
		
		uri = new URI("/api/guests/");
		
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
		guest.setEmail("washington1@orkut.com");
		guest.setName("Washington");
		guest.setLastName("Ignácio");
		guest.setTitle("MRS.");
		guest.setPassword("1234567");
		
		addressRepository.save(address);
		guest = guestRespository.save(guest);
		
		Guest guest2 = guestRespository.findById(guest.getId()).get();
		guest2.setId(null);
		guest2.setName("Antonio");
		guest2.setLastName("Ferreira");
		guestRespository.save(guest2);
	}
	
	@Test
	public void shouldReturnAllGuestsWithoutParamAndStatusOk() throws Exception {
		
		MvcResult result = 
				mockMvc.perform(get(uri)
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		GuestDto[] guestObjResponse = objectMapper.readValue(contentAsString, GuestDto[].class);
		
		/// Verify request succeed
		assertEquals(3, guestObjResponse.length);
	}
	
	@Test
	public void shouldReturnOneGuestByParamAndStatusOk() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Washington")
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		GuestDto[] guestObjResponse = objectMapper.readValue(contentAsString, GuestDto[].class);
		
		/// Verify request succeed
		assertEquals(1, guestObjResponse.length);
		assertEquals(guest.getName(), guestObjResponse[0].getName());
		assertEquals(guest.getAddress().getCity(), guestObjResponse[0].getAddress().getCity());
	}
	
	@Test
	public void shouldReturnOneGuestByIdAndStatusOk() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri + guest.getId().toString())
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		GuestDto guestObjResponse = objectMapper.readValue(contentAsString, GuestDto.class);
		
		/// Verify request succeed
		assertEquals(guest.getName(), guestObjResponse.getName());
		assertEquals(guest.getAddress().getCity(), guestObjResponse.getAddress().getCity());

	}


	@Test
	public void shouldReturEmptyBodyByWrongParam() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Washington222")
						.headers(headers))
						.andDo(print())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		GuestDto[] guestObjResponse = objectMapper.readValue(contentAsString, GuestDto[].class);
		
		assertEquals(0, guestObjResponse.length);
	}

}