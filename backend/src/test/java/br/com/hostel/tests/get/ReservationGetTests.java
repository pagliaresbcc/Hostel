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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.model.CheckPayment;
import br.com.hostel.model.Customer;
import br.com.hostel.model.Reservation;
import br.com.hostel.repository.CustomerRepository;
import br.com.hostel.repository.PaymentsRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ReservationGetTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static URI uri;
	private static HttpHeaders headers = new HttpHeaders();
	private static Reservation reservation;
	
	@BeforeAll
    public static void beforeAll(@Autowired ReservationRepository reservationRepository, 
    		@Autowired PaymentsRepository paymentsRepository, @Autowired CustomerRepository customerRepository, 
    		@Autowired RoomRepository roomRepository, @Autowired MockMvc mockMvc, 
    		@Autowired ObjectMapper objectMapper) throws JsonProcessingException, Exception {
		
		CheckPayment checkPayment = new CheckPayment();
		ReservationForm reservationForm = new ReservationForm();
		List<Long> rooms_ID = new ArrayList<>();
		Customer customer = new Customer();
		Set<Reservation> reservationsList = new HashSet<>();
		LoginForm login = new LoginForm();
		
		uri = new URI("/api/reservations/");
		
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

		//setting reservation object
		reservationForm.setCheckinDate(LocalDate.of(2021, 04, 01));
		reservationForm.setCheckoutDate(LocalDate.of(2021, 04, 04));
		reservationForm.setNumberOfGuests(4);
		reservationForm.setCustomer_ID(1L);
		
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

		reservationForm.setCheckinDate(LocalDate.of(2021, 05, 01));
		reservationForm.setCheckoutDate(LocalDate.of(2021, 05, 04));
		
		reservation = reservationRepository.save(reservationForm.returnReservation(paymentsRepository, roomRepository));
		reservationsList.add(reservation);
		
		customer = customerRepository.findById(reservationForm.getCustomer_ID()).get();
		customer.setReservations(reservationsList);
		customerRepository.save(customer);    
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

		/// Verify request succeed
		assertEquals(2, reservationObjResponse.length);
		assertEquals(reservationObjResponse[0].getPayments().getAmount(), reservation.getPayment().getAmount());
	}
	
	@Test
	public void shouldReturnAllCustomerReservationsByName() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Aluno")
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto[] reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto[].class);

		/// Verify request succeed
		assertEquals(2, reservationObjResponse.length);
		assertEquals(reservationObjResponse[0].getPayments().getAmount(), reservation.getPayment().getAmount());
		assertEquals(reservationObjResponse[0].getCheckinDate(), reservation.getCheckinDate());
	}

	@Test
	public void shouldReturnOneReservationAndStatusOkById() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri + "1")
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto[] reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto[].class);

		/// Verify request succeed
		assertEquals(reservationObjResponse[0].getPayments().getAmount(), reservation.getPayment().getAmount());
		assertEquals(reservationObjResponse[0].getCheckinDate(), reservation.getCheckinDate());
	}

	@Test
	public void shouldReturnNotFoundStatusAndNullBodyByWrongParam() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Aluno333")
						.headers(headers))
						.andDo(print())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto[] reservationObjResponse = objectMapper.readValue(contentAsString, ReservationDto[].class);
		
		assertEquals(0, reservationObjResponse.length);
	}
}
