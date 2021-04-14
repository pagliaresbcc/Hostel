package br.com.hostel.tests.unit.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.RoomUpdateForm;
import br.com.hostel.initializer.RoomInitializer;
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
	private static DailyRate dailyRate = new DailyRate();

	@BeforeAll
	public static void beforeAll(@Autowired RoomRepository roomRepository,
			@Autowired DailyRateRepository dailyRateRepository, @Autowired MockMvc mockMvc, 
			@Autowired ObjectMapper objectMapper) throws JsonProcessingException, Exception {
		
		uri = new URI("/api/rooms/");
		
		RoomInitializer.initialize(headers, room, dailyRate, mockMvc, objectMapper);
		
		dailyRateRepository.save(dailyRate);
		roomRepository.save(room);
	}
	
	@Test
	public void shouldAutenticateAndUpdateRoomDescriptionAndDailyRate() throws Exception {
		
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
	
	@Test
	public void shouldReturnBadRequestStatusWhenUpdatingARoomWithNonExistentId() throws Exception {
		
		RoomUpdateForm roomToUpdate = new RoomUpdateForm();
		roomToUpdate.setDescription("test update method");
		roomToUpdate.setDailyRate(room.getDailyRate());
		roomToUpdate.getDailyRate().setPrice(1500.0);
		
		mockMvc
			.perform(put(uri + "999")
				.headers(headers)
				.content(objectMapper.writeValueAsString(roomToUpdate)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andReturn();
	}
}