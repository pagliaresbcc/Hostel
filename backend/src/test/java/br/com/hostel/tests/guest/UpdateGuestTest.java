package br.com.hostel.tests.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import br.com.hostel.controller.form.GuestUpdateForm;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.tests.initializer.GuestsInitializer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UpdateGuestTest {
	
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
	public static void beforeAll(@Autowired GuestRepository guestRespository, @Autowired AddressRepository addressRepository, 
			@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws JsonProcessingException, Exception  {
		uri = new URI("/api/guests/");
		
		GuestsInitializer.initialize(headers, address, guest, mockMvc, objectMapper);
		
		addressRepository.save(address);
		guestRespository.save(guest);
	}
	
	@Test
	public void shouldAutenticateAndDeleteOneRoomWithId2() throws Exception {
		
		GuestUpdateForm guestToUpdate = new GuestUpdateForm();
		guestToUpdate.setLastName("Silva");
		guestToUpdate.setTitle("Sir");
		
		MvcResult result = mockMvc.perform(put(uri + guest.getId().toString())
				.headers(headers)
				.content(objectMapper.writeValueAsString(guestToUpdate)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		
		GuestDto roomObjResponse = objectMapper.readValue(contentAsString, GuestDto.class);
		
		assertEquals(roomObjResponse.getLastName(), guestToUpdate.getLastname());
		assertEquals(roomObjResponse.getTitle(), guestToUpdate.getTitle());
	}
}