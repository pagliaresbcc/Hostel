package br.com.hostel.tests.guest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.tests.initializer.GuestsInitializer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DeleteGuestsTest {

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
}