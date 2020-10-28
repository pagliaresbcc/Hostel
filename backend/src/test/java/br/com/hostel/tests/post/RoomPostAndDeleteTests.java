package br.com.hostel.tests.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RoomPostAndDeleteTests {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired 
	private DailyRateRepository dailyRateRepository;
	
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
		
		LoginForm login = new LoginForm();
		uri = new URI("/api/rooms/");

		//setting login variables to autenticate
		login.setEmail("aluno@email.com");
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
		
		room.setDescription("room test");
		room.setNumber(34);
		room.setDimension(230.0);
		room.setMaxNumberOfGuests(4);
		room.setDailyRate(dailyRate);
	}

	@Test
	public void shouldReturnNotFoundStatusWhenDeletingByNonExistentRoomID() throws Exception {
		dailyRateRepository.save(dailyRate);
		roomRepository.save(room);
		
		mockMvc.perform(delete(uri + "0")
			.headers(headers))
			.andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
	}

	@Test
	public void shouldAutenticateAndDeleteOneRoomWithId2() throws Exception {
		dailyRateRepository.save(dailyRate);
		roomRepository.save(room);
		
		mockMvc.perform(delete(uri + room.getId().toString())
			.headers(headers))
			.andDo(print())
            .andExpect(status().isOk())
            .andReturn();
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