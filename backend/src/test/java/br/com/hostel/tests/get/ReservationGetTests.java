package br.com.hostel.tests.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.model.CashPayment;
import br.com.hostel.model.Customer;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import br.com.hostel.repository.CustomerRepository;
import br.com.hostel.repository.ReservationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ReservationGetTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private ReservationRepository reservationRepository;
	
	@Autowired
	ObjectMapper objectMapper;

	private URI uri;
	private Reservation reservation = new Reservation();
	private CashPayment cash = new CashPayment();
	private Room room = new Room(13, 230.0, new DailyRate(400.0));
	private List<Reservation> reservationsList = new ArrayList<>();
	
	private Customer customer = Mockito.mock(Customer.class);
	private List<Customer> customersList = new ArrayList<>();
	
	@Before
	public void init() throws URISyntaxException {
		uri = new URI("/api/reservations");

		cash.setAmountTendered(5000.0);
		cash.setAmount(5000.0);
		cash.setDate(LocalDateTime.now());

		reservation.setCheckinDate(LocalDate.of(2012, 12, 12));
		reservation.setCheckoutDate(LocalDate.of(2012, 12, 17));
		reservation.setReservationDate(LocalDate.now());
		reservation.setNumberOfGuests(2);
		reservation.setPayment(cash);
		reservation.addRoom(room);
		
		reservationsList.add(reservation);
		
		customer.addReservation(reservation);
		customersList.add(customer);
	}

	@Test
	public void shouldReturnOneReservationAndStatusOkWithoutParam() throws Exception {
		
		Reservation reservation2 = reservation;
		reservation2.setNumberOfGuests(25);
		reservationsList.add(reservation2);

		Mockito.when(reservationRepository.findAll()).thenReturn(reservationsList);

		MvcResult result = 
				mockMvc.perform(get(uri))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto[] customerObjResponse = objectMapper.readValue(contentAsString, ReservationDto[].class);

		/// Verify request succeed
		assertEquals(customerObjResponse[0].getPayments().getAmount(), 5000, 0);
		assertEquals(customerObjResponse.length, 2);
		

	}

	@Test
	public void shouldReturnOneReservationAndStatusOkByParam() throws Exception {

		Mockito.when(customerRepository.findByName("Teste")).thenReturn(customersList);
		Mockito.when(customer.getReservations()).thenReturn(reservationsList.stream().collect(Collectors.toSet()));
		

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Teste"))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto[] customerObjResponse = objectMapper.readValue(contentAsString, ReservationDto[].class);

		/// Verify request succeed
		assertEquals(customerObjResponse[0].getPayments().getAmount(), 5000, 0);
		assertEquals(customerObjResponse[0].getCheckinDate(), LocalDate.of(2012, 12, 12));
	}

	@Test
	public void shouldReturnOneReservationAndStatusOkById() throws Exception {

		Mockito.when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));


		MvcResult result = 
				mockMvc.perform(get(uri+"/2"))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		ReservationDto customerObjResponse = objectMapper.readValue(contentAsString, ReservationDto.class);

		/// Verify request succeed
		assertEquals(customerObjResponse.getPayments().getAmount(), 5000, 0);
		assertEquals(customerObjResponse.getCheckinDate(), LocalDate.of(2012, 12, 12));
	}

	@Test
	public void shouldReturnNotFoundStatusAndNullBodyByWrongParam() throws Exception {

		Mockito.when(customerRepository.findByName("Teste")).thenReturn(customersList);
		Mockito.when(customer.getReservations()).thenReturn(reservationsList.stream().collect(Collectors.toSet()));

		mockMvc.perform(get(uri)
				.param("name", "Teste333"))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andReturn();
	}
}