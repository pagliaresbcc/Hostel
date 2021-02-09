package br.com.hostel.tests.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.GuestDto;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.tests.initializer.GuestsInitializer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CreateGuestsTest {

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
		
		GuestsInitializer.initialize(headers, address, guest, mockMvc, objectMapper);
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