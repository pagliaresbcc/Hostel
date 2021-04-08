package br.com.hostel.tests.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import br.com.hostel.initializer.GuestsInitializer;
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
		
		uri = new URI("/api/guests/");
		
		GuestsInitializer.initialize(headers, address, guest, mockMvc, objectMapper);
		
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
		
		assertEquals(3, guestObjResponse.length);
	}
	
	@Test
	public void shouldReturnOneGuestAndStatusOkByParam() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Washington")
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		GuestDto[] guestObjResponse = objectMapper.readValue(contentAsString, GuestDto[].class);
		
		assertEquals(1, guestObjResponse.length);
		assertEquals(guest.getName(), guestObjResponse[0].getName());
		assertEquals(guest.getAddress().getCity(), guestObjResponse[0].getAddress().getCity());
	}
	
	@Test
	public void shouldReturnOneGuestAndStatusOkByID() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri + guest.getId().toString())
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		GuestDto guestObjResponse = objectMapper.readValue(contentAsString, GuestDto.class);
		
		assertEquals(guest.getName(), guestObjResponse.getName());
		assertEquals(guest.getAddress().getCity(), guestObjResponse.getAddress().getCity());

	}


	@Test
	public void shouldReturnNotFoundStatusByUsingWrongParam() throws Exception {

		mockMvc
			.perform(get(uri + "999")
				.headers(headers))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andReturn();
	}

}