package br.com.hostel.tests.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.model.CheckPayment;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RoomGetTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private static URI uri;
	private static HttpHeaders headers = new HttpHeaders();
	
	@BeforeAll
	public static void beforeAll(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper)
			throws JsonProcessingException, Exception {
		
		ReservationForm reservationForm = new ReservationForm();
		CheckPayment checkPayment = new CheckPayment();
		List<Long> rooms_ID = new ArrayList<>();
		LoginForm login = new LoginForm();

		uri = new URI("/api/rooms/");

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
		
		//setting reservation object
		reservationForm.setCheckinDate(LocalDate.of(2021, 4, 10));
		reservationForm.setCheckoutDate(LocalDate.of(2021, 4, 17));
		reservationForm.setNumberOfGuests(2);
		reservationForm.setGuest_ID(1L);
		
		checkPayment.setAmount(3000);
		checkPayment.setDate(LocalDateTime.of(LocalDate.of(2020, 01, 25), LocalTime.of(21, 31)));
		checkPayment.setBankId("01");
		checkPayment.setBankName("Banco do Brasil");
		checkPayment.setBranchNumber("1234-5");
		
		reservationForm.setPayment(checkPayment);
		
		rooms_ID.add(1L);
		rooms_ID.add(2L);
		reservationForm.setRooms_ID(rooms_ID);

		mockMvc.perform(post("/api/reservations/")
				.headers(headers)
				.content(objectMapper.writeValueAsString(reservationForm)));
	}
	
	@Test
	public void shouldShowOnlyAvailableRoomsWithReservationCheckinAndCheckoutDateBefore() throws Exception {
		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("checkinDate","2021-04-01")
						.param("checkoutDate","2021-04-09")
						.param("numberOfGuests","2")
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto[] roomObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);
		
		assertEquals(5, roomObjResponse.length);
	}
	
	@Test
	public void shouldShowOnlyAvailableRoomsWithReservationCheckinAndCheckoutDateAfter() throws Exception {
		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("checkinDate","2021-04-18")
						.param("checkoutDate","2021-04-25")
						.param("numberOfGuests","2")
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto[] roomObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);
		
		assertEquals(5, roomObjResponse.length);
	}
	
	@Test
	public void shouldShowOnlyAvailableRoomsWithReservationCheckinDateBeforeAndCheckoutDateBetween() throws Exception {
		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("checkinDate","2021-04-05")
						.param("checkoutDate","2021-04-15")
						.param("numberOfGuests","2")
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
				
		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto[] roomObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);
		
		assertEquals(3, roomObjResponse.length);
	}
	
	@Test
	public void shouldShowOnlyAvailableRoomsWithReservationCheckinDateBetweenAndCheckoutDateAfter() throws Exception {
		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("checkinDate","2021-04-15")
						.param("checkoutDate","2021-04-20")
						.param("numberOfGuests","2")
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto[] roomObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);
		
		assertEquals(3, roomObjResponse.length);
	}
	
	@Test
	public void shouldShowOnlyAvailableRoomsWithReservationCheckinDateBeforeAndCheckoutDateAfter() throws Exception {
		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("checkinDate","2021-04-05")
						.param("checkoutDate","2021-04-20")
						.param("numberOfGuests","2")
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto[] roomObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);
		
		assertEquals(3, roomObjResponse.length);
	}
	
	@Test
	public void shouldShowOnlyAvailableRoomsWithReservationCheckinDateAndCheckoutDateBetween() throws Exception {
		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("checkinDate","2021-04-13")
						.param("checkoutDate","2021-04-16")
						.param("numberOfGuests","2")
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto[] roomObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);
		
		assertEquals(3, roomObjResponse.length);
	}
	
	@Test
	public void shouldReturnAllRoomsAndStatusOkWithoutParam() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto[] roomObjResponse = objectMapper.readValue(contentAsString, RoomDto[].class);

		/// Verify request succeed
		assertEquals(5, roomObjResponse.length);
		assertEquals(13, roomObjResponse[0].getNumber());
		assertEquals(250, roomObjResponse[1].getDimension());
	}
	
	@Test
	public void shouldReturnOneRoomAndStatusOkById() throws Exception {
		
		MvcResult result = 
				mockMvc.perform(get(uri + "1")
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		
		RoomDto roomObjResponse = objectMapper.readValue(contentAsString, RoomDto.class);
		
		/// Verify request succeed
		assertEquals(13, roomObjResponse.getNumber());
		assertEquals(500, roomObjResponse.getDimension());
	}
}