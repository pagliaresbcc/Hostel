package br.com.hostel.tests.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.RoomRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RoomGetTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private RoomRepository roomRepository;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private URI uri;
	private Room room;	
	private List<Room> roomList = new ArrayList<>();
	
	
	@Before
	public void init() throws URISyntaxException {

		uri = new URI("/api/rooms");

		room = new Room(13, 230.0, new DailyRate(400.0));
		
		roomList.add(room);
	}
	
	@Test
	public void shouldReturnAllRoomsAndStatusOkWithoutParam() throws Exception {
		Room room2 = new Room(14, 250.0, new DailyRate(500.0));
		roomList.add(room2);
		
		Mockito.when(roomRepository.findAll()).thenReturn(roomList);

		MvcResult result = 
				mockMvc.perform(get(uri))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto[] customerObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);
		System.out.println(">>>>>>>>>>>>>>>>"+customerObjResponse[0].getDailyRate().getPrice());
		System.out.println(">>>>>>>>>>>>>>>>>>"+customerObjResponse[1].getDailyRate().getPrice());
		/// Verify request succeed
		assertEquals(customerObjResponse.length, 2);
		assertEquals(customerObjResponse[0].getNumber(), 13);
		assertEquals(customerObjResponse[1].getDailyRate().getPrice(), 500, 0);
	}
	
	@Test
	public void shouldReturnOneRoomAndStatusOkByParam() throws Exception {
		
		Mockito.when(roomRepository.findByNumber(13)).thenReturn(roomList);

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("number", "13"))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		RoomDto[] customerObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);
		
		/// Verify request succeed
		assertEquals(customerObjResponse[0].getNumber(), 13);
		assertEquals(customerObjResponse[0].getDimension(), 230, 0);
	}
	
	@Test
	public void shouldReturnOneRoomAndStatusOkById() throws Exception {
		
		Mockito.when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

		MvcResult result = 
				mockMvc.perform(get(uri+"/1"))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		RoomDto customerObjResponse = objectMapper.readValue(contentAsString, RoomDto.class);
		
		/// Verify request succeed
		assertEquals(customerObjResponse.getNumber(), 13);
		assertEquals(customerObjResponse.getDimension(), 230, 0);
	}
	
	@Test
	public void shouldReturnNotFoundStatusAndNullBodyByWrongParam() throws Exception {
		
		Mockito.when(roomRepository.findByNumber(13)).thenReturn(roomList);

		mockMvc.perform(get(uri)
				.param("number", "333"))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andReturn();
	}
}