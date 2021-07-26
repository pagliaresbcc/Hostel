package br.com.hostel.tests.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.controller.form.ReservationUpdateForm;
import br.com.hostel.initializer.ReservationInitializer;
import br.com.hostel.model.CheckPayment;
import br.com.hostel.model.Reservation;
import br.com.hostel.repository.PaymentRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UpdateReservationTest {

	@Autowired
	ReservationRepository reservationRepository;
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private URI uri;
	private HttpHeaders headers = new HttpHeaders();
	private ReservationForm reservationForm = new ReservationForm();
	private CheckPayment checkPayment = new CheckPayment();
	private List<Long> rooms_ID = new ArrayList<>();
	private Reservation reservation;
	
	@BeforeEach
	public void init() throws JsonProcessingException, Exception {
		
		uri = new URI("/api/reservations/");
		
		ReservationInitializer.initialize(headers, reservationForm, checkPayment, rooms_ID, mockMvc, objectMapper);
		
		paymentRepository.save(checkPayment);
		reservation = reservationRepository.save(reservationForm.returnReservation(paymentRepository, roomRepository));
	}
	
	@Test
	public void shouldAutenticateAndUpdateReservationInformations() throws Exception {

		ReservationUpdateForm rsvToUpdate = new ReservationUpdateForm();
		rsvToUpdate.setNumberOfGuests(3);
		rsvToUpdate.setPayment(reservation.getPayment());
		rsvToUpdate.getPayment().setAmount(5500);
		rsvToUpdate.setRooms_ID(rooms_ID);
		
		MvcResult result = 
				mockMvc
					.perform(put(uri + reservation.getId().toString())
					.headers(headers)
					.content(objectMapper.writeValueAsString(rsvToUpdate)))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto.class);
		
		assertEquals(reservationObjResponse.getCheckinDate(), reservation.getCheckinDate());
		assertEquals(reservationObjResponse.getPayment().getAmount(), rsvToUpdate.getPayment().getAmount());
		assertEquals(rsvToUpdate.getRooms_ID().size(), reservationObjResponse.getRooms().size());
	}
	
	@Test
	public void shouldReturnNotFoundStatusWhenUpdatingAReservationWithNonExistentID() throws Exception {
		
		ReservationUpdateForm rsvToUpdate = new ReservationUpdateForm();
		rsvToUpdate.setNumberOfGuests(3);
		rsvToUpdate.setPayment(reservation.getPayment());
		rsvToUpdate.getPayment().setAmount(5500);
		rsvToUpdate.setRooms_ID(rooms_ID);
		
		mockMvc
			.perform(put(uri + "999")
				.headers(headers)
				.content(objectMapper.writeValueAsString(rsvToUpdate)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	public void shouldReturnBadRequestStatusWhenUpdateWithEmptyReservationRoomsList() throws Exception {
		
		ReservationUpdateForm rsvToUpdate = new ReservationUpdateForm();
		rsvToUpdate.setNumberOfGuests(3);
		rsvToUpdate.setPayment(reservation.getPayment());
		rsvToUpdate.getPayment().setAmount(5500);
		rsvToUpdate.setRooms_ID(null);
		
		reservation.setRooms(null);
		
		reservationRepository.save(reservation);
		
		mockMvc
			.perform(put(uri + reservation.getId().toString())
				.headers(headers)
				.content(objectMapper.writeValueAsString(rsvToUpdate)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();
	}
}