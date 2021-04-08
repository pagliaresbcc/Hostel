package br.com.hostel.tests.room;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
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

import br.com.hostel.initializer.RoomInitializer;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DeleteRoomsTest {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired 
	private DailyRateRepository dailyRateRepository;
	
	@Autowired 
	private MockMvc mockMvc;

	private URI uri;
	private HttpHeaders headers = new HttpHeaders();
	private Room room = new Room();
	private DailyRate dailyRate = new DailyRate();

	@BeforeEach
	public void beforeEach(@Autowired ObjectMapper objectMapper, 
			@Autowired MockMvc mockMvc) throws JsonProcessingException, Exception {
		
		uri = new URI("/api/rooms/");

		RoomInitializer.initialize(headers, room, dailyRate, mockMvc, objectMapper);
	}

	@Test
	public void shouldReturnNotFoundStatusWhenDeletingARoomWithNonExistentID() throws Exception {
		dailyRateRepository.save(dailyRate);
		roomRepository.save(room);
		
		mockMvc.perform(delete(uri + "0")
				.headers(headers))
				.andDo(print())
	            .andExpect(status().isNotFound());
	}

	@Test
	public void shouldAutenticateAndDeleteOneRoomWithId2() throws Exception {
		dailyRateRepository.save(dailyRate);
		roomRepository.save(room);
		
		mockMvc.perform(delete(uri + room.getId().toString())
				.headers(headers))
				.andDo(print())
	            .andExpect(status().isOk());
	}
}