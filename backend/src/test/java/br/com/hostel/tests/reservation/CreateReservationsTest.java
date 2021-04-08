package br.com.hostel.tests.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.initializer.ReservationInitializer;
import br.com.hostel.model.CashPayment;
import br.com.hostel.model.CheckPayment;
import br.com.hostel.model.CreditCardPayment;
import br.com.hostel.repository.PaymentsRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CreateReservationsTest {

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
	private ReservationForm reservationForm = new ReservationForm();
	private CheckPayment checkPayment = new CheckPayment();
	private CashPayment cashPayment = new CashPayment();
	private CreditCardPayment creditCardPayment = new CreditCardPayment();
	private List<Long> rooms_ID = new ArrayList<>();
	
	@BeforeEach
	public void init() throws JsonProcessingException, Exception {
		
		uri = new URI("/api/reservations/");
		
		ReservationInitializer.initialize(headers, reservationForm, checkPayment, rooms_ID, mockMvc, objectMapper);
	}
	
	@Test
	public void shouldReturnNotFoundStatusWithNonExistentGuestID() throws Exception {
		
		reservationForm.setGuest_ID(33L);
		
		mockMvc.perform(post(uri)
				.headers(headers)
				.content(objectMapper.writeValueAsString(reservationForm)))
				.andDo(print())
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldReturnBadRequestStatusWithReservationCheckinDateOlderActualDate() throws Exception {
		
		reservationForm.setCheckinDate(LocalDate.of(2019, 04, 01));
		
		mockMvc.perform(post(uri)
				.headers(headers)
				.content(objectMapper.writeValueAsString(reservationForm)))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void shouldReturnBadRequestStatusWithReservationCheckoutDateOlderThanCheckinDate() throws Exception {
		
		reservationForm.setCheckinDate(LocalDate.of(2025, 04, 01));
		reservationForm.setCheckoutDate(LocalDate.of(2025, 03, 01));
		
		mockMvc.perform(post(uri)
				.headers(headers)
				.content(objectMapper.writeValueAsString(reservationForm)))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void shouldAutenticateAndCreateOneReservationUsingCheckPaymentAndReturnStatusCreated() throws Exception {

		MvcResult result = 
				mockMvc
					.perform(post(uri)
					.headers(headers)
					.content(objectMapper.writeValueAsString(reservationForm)))
					.andDo(print())
					.andExpect(status().isCreated())
					.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto.class);
		
		CheckPayment checkObjResponse = (CheckPayment) reservationObjResponse.getPayments();

		assertEquals(reservationObjResponse.getCheckinDate(), LocalDate.of(2025, 04, 01));
		assertEquals(reservationObjResponse.getPayments().getAmount(), 3000);
		assertEquals(checkObjResponse.getBankName(), "Banco do Brasil");
	}
	
	@Test
	public void shouldAutenticateAndCreateOneReservationUsingCashPaymentAndReturnStatusCreated() throws Exception {
		cashPayment.setAmount(4000);
		cashPayment.setAmountTendered(10000);
		cashPayment.setDate(LocalDateTime.of(LocalDate.of(2025,01,25), LocalTime.of(21, 32)));
		
		reservationForm.setPayment(cashPayment);
		
		rooms_ID.add(3L);
		reservationForm.setRooms_ID(rooms_ID);
		
		MvcResult result = 
				mockMvc
					.perform(post(uri)
					.headers(headers)
					.content(objectMapper.writeValueAsString(reservationForm)))
					.andDo(print())
					.andExpect(status().isCreated())
					.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto.class);
		
		CashPayment cashObjResponse = (CashPayment) reservationObjResponse.getPayments();

		assertEquals(reservationObjResponse.getCheckinDate(), LocalDate.of(2025, 04, 01));
		assertEquals(reservationObjResponse.getPayments().getAmount(), 4000);
		assertEquals(cashObjResponse.getAmountTendered(), 10000);
	}
	
	@Test
	public void shouldAutenticateAndCreateOneReservationUsingCreditCardPaymentAndReturnStatusCreated() throws Exception {
		creditCardPayment.setAmount(5000);
		creditCardPayment.setDate(LocalDateTime.of(LocalDate.of(2025,01,25), LocalTime.of(21, 33)));
		creditCardPayment.setIssuer("VISA");
		creditCardPayment.setNameOnCard("WASHINGTON A SILVA");
		creditCardPayment.setCardNumber("1234 5678 9101 1121");
		creditCardPayment.setExpirationDate(LocalDate.of(2048, 05, 01));
		creditCardPayment.setSecurityCode("123");
		
		reservationForm.setPayment(creditCardPayment);
		
		rooms_ID.add(4L);
		rooms_ID.add(5L);
		reservationForm.setRooms_ID(rooms_ID);
		
		MvcResult result = 
				mockMvc
				.perform(post(uri)
					.headers(headers)
					.content(objectMapper.writeValueAsString(reservationForm)))
					.andDo(print())
					.andExpect(status().isCreated())
					.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();
		
		ReservationDto reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto.class);
		
		CreditCardPayment creditCardObjResponse = (CreditCardPayment) reservationObjResponse.getPayments();
		
		assertEquals(reservationObjResponse.getCheckinDate(), LocalDate.of(2025, 04, 01));
		assertEquals(reservationObjResponse.getPayments().getAmount(), 5000);
		assertEquals(creditCardObjResponse.getNameOnCard(), "WASHINGTON A SILVA");
	}
}