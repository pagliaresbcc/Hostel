package br.com.hostel.tests.room;

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

import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.tests.initializer.RoomInitializer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CreateRoomsTest {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired 
	private MockMvc mockMvc;

	private static URI uri;
	private static HttpHeaders headers = new HttpHeaders();
	private static Room room = new Room();
	private static DailyRate dailyRate = new DailyRate(400);

	@BeforeAll
	public static void beforeAll(@Autowired ObjectMapper objectMapper, 
			@Autowired MockMvc mockMvc) throws JsonProcessingException, Exception {
		
		uri = new URI("/api/rooms/");

		RoomInitializer.initialize(headers, room, dailyRate, mockMvc, objectMapper);
	}

	@Test
	public void shouldAutenticateAndCreateOneRoomAndReturnStatusCreated() throws Exception {

		MvcResult result = mockMvc.perform(post(uri)
						.headers(headers)
						.content(objectMapper.writeValueAsString(room)))
						.andDo(print())
						.andExpect(status().isCreated())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		RoomDto roomObjResponse = objectMapper.readValue(contentAsString, RoomDto.class);

		assertEquals(room.getNumber(), roomObjResponse.getNumber());
		assertEquals(room.getDimension(), roomObjResponse.getDimension(), 230);
	}
}