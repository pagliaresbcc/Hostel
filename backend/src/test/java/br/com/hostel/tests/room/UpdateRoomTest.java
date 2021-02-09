package br.com.hostel.tests.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.controller.form.RoomUpdateForm;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UpdateRoomTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	private static URI uri;
	private static HttpHeaders headers = new HttpHeaders();
	private static Room room = new Room();
	
	@BeforeAll
	public static void beforeAll(@Autowired RoomRepository roomRepository,
			@Autowired DailyRateRepository dailyRateRepository, @Autowired MockMvc mockMvc, 
			@Autowired ObjectMapper objectMapper) throws JsonProcessingException, Exception {
		
		uri = new URI("/api/rooms/");
		
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
		
		DailyRate dailyRate = new DailyRate(400);
		dailyRateRepository.save(dailyRate);
		
		room.setDescription("room test");
		room.setNumber(55);
		room.setDimension(230.0);
		room.setMaxNumberOfGuests(4);
		room.setDailyRate(dailyRate);
		
		room = roomRepository.save(room);
	}
	
	@Test
	public void shouldAutenticateAndDeleteOneRoomWithId2() throws Exception {
		
		RoomUpdateForm roomToUpdate = new RoomUpdateForm();
		roomToUpdate.setDescription("test update method");
		roomToUpdate.setDailyRate(room.getDailyRate());
		roomToUpdate.getDailyRate().setPrice(1500.0);
		
		MvcResult result = mockMvc.perform(put(uri + room.getId().toString())
				.headers(headers)
				.content(objectMapper.writeValueAsString(roomToUpdate)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto roomObjResponse = objectMapper.readValue(contentAsString, RoomDto.class);
		
		assertEquals(roomObjResponse.getNumber(), room.getNumber());
		assertEquals(roomObjResponse.getDailyRate().getPrice(), roomToUpdate.getDailyRate().getPrice());
		assertTrue(roomObjResponse.getDescription().compareTo(room.getDescription()) != 0);
	}
}