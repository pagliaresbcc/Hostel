package br.com.hostel.tests.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.initializer.ReservationInitializer;
import br.com.hostel.model.CheckPayment;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.repository.PaymentRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ListReservationsTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static URI uri;
	private static HttpHeaders headers = new HttpHeaders();
	private static Reservation reservation1, reservation2;
	private static CheckPayment checkPayment = new CheckPayment();
	private static ReservationForm reservationForm = new ReservationForm();
	private static Guest guest = new Guest();
	private static List<Room> reservation2RoomsList;
	private static List<Long> rooms_ID = new ArrayList<>();
	private static Set<Reservation> reservationsList = new HashSet<>();
	
	@BeforeAll
    public static void beforeAll(@Autowired ReservationRepository reservationRepository, 
    		@Autowired PaymentRepository paymentRepository, @Autowired GuestRepository guestRepository, 
    		@Autowired RoomRepository roomRepository, @Autowired MockMvc mockMvc, 
    		@Autowired ObjectMapper objectMapper) throws JsonProcessingException, Exception {
		
		uri = new URI("/api/reservations/");
		
		ReservationInitializer.initialize(headers, reservationForm, checkPayment, rooms_ID, mockMvc, objectMapper);
		
		paymentRepository.save(reservationForm.getPayment());
		
		reservation1 = reservationRepository.save(reservationForm.returnReservation(paymentRepository, roomRepository));
		reservationsList.add(reservation1);

		reservationForm.setCheckinDate(LocalDate.of(2025, 05, 01));
		reservationForm.setCheckoutDate(LocalDate.of(2025, 05, 04));
		rooms_ID.remove(2L);
		rooms_ID.add(3L);
		reservation2 = reservationRepository.save(reservationForm.returnReservation(paymentRepository, roomRepository));
		reservationsList.add(reservation2);
		
		guest = guestRepository.findById(reservationForm.getGuest_ID()).get();
		guest.setReservations(reservationsList);
		guestRepository.save(guest);   
		
		reservation2RoomsList = reservation2.getRooms().stream().collect(Collectors.toList());
	}
	
	@Test
	public void shouldReturnAllReservationsWithoutParam() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.headers(headers))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto[] reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto[].class);

		assertTrue(reservationObjResponse.length > 1);
	}
	
	@Test
	public void shouldReturnAllReservationsByGuestName() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "admin")
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto[] reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto[].class);

		assertEquals(reservation1.getCheckinDate(), reservationObjResponse[0].getCheckinDate());
		assertEquals(reservation2.getCheckoutDate(), reservationObjResponse[1].getCheckoutDate());
		assertEquals(reservation2RoomsList.get(0).getNumber(), reservationObjResponse[1].getRooms().stream()
																						.collect(Collectors.toList())
																						.get(0).getNumber());
		assertEquals(2, reservationObjResponse.length);
	}

	@Test
	public void shouldReturnNotFoundStatusAndNullBodyByUsingWrongParam() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "admin333")
						.headers(headers))
						.andDo(print())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto[] reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto[].class);
		
		assertEquals(0, reservationObjResponse.length);
	}
}
