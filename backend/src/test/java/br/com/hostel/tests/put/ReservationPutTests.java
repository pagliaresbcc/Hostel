package br.com.hostel.tests.put;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.controller.form.ReservationUpdateForm;
import br.com.hostel.model.CheckPayment;
import br.com.hostel.model.Reservation;
import br.com.hostel.repository.PaymentsRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ReservationPutTests {

	@Autowired
	ReservationRepository reservationRepository;
	
	@Autowired
	PaymentsRepository paymentsRepository;
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private URI uri;
	private HttpHeaders headers = new HttpHeaders();
	private LoginForm login = new LoginForm();
	private ReservationForm reservationForm = new ReservationForm();
	private CheckPayment checkPayment = new CheckPayment();
	private List<Long> rooms_ID = new ArrayList<>();
	private Reservation reservation;
	
	@BeforeEach
	public void init() throws JsonProcessingException, Exception {
		
		uri = new URI("/api/reservations/");
		
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
		reservationForm.setCheckinDate(LocalDate.of(2021, 04, 01));
		reservationForm.setCheckoutDate(LocalDate.of(2021, 04, 04));
		reservationForm.setNumberOfGuests(4);
		reservationForm.setGuest_ID(1L);
		
		checkPayment.setAmount(3000);
		checkPayment.setDate(LocalDateTime.of(LocalDate.of(2020, 01, 25), LocalTime.of(21, 31)));
		checkPayment.setBankId("01");
		checkPayment.setBankName("Banco do Brasil");
		checkPayment.setBranchNumber("1234-5");
		
		reservationForm.setPayment(checkPayment);
		
		rooms_ID.add(2L);
		reservationForm.setRooms_ID(rooms_ID);
		
		paymentsRepository.save(reservationForm.getPayment());
		reservation = reservationRepository.save(reservationForm.returnReservation(paymentsRepository, roomRepository));
	}
	
	@Test
	public void shouldAutenticateAndUpdateReservation() throws Exception {

		ReservationUpdateForm rsvToUpdate = new ReservationUpdateForm();
		rsvToUpdate.setNumberOfGuests(3);
		rsvToUpdate.setPayment(reservation.getPayment());
		rsvToUpdate.getPayment().setAmount(5500);
		rsvToUpdate.setRooms_ID(rooms_ID);
		
		MvcResult result = 
				mockMvc
					.perform(put(uri+reservation.getId().toString())
					.headers(headers)
					.content(objectMapper.writeValueAsString(rsvToUpdate)))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto.class);
		
		assertEquals(reservationObjResponse.getCheckinDate(), reservation.getCheckinDate());
		assertEquals(reservationObjResponse.getPayments().getAmount(), rsvToUpdate.getPayment().getAmount());
		assertEquals(rsvToUpdate.getRooms_ID().size(), reservationObjResponse.getRooms().size());
		assertTrue(reservationObjResponse.getNumberOfGuests() != reservation.getNumberOfGuests());
	}
}