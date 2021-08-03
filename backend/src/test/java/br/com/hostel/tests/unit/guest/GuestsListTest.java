package br.com.hostel.tests.unit.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hostel.exceptions.BaseException;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.helper.Role;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GuestService.class)
public class GuestsListTest {

	@MockBean
	GuestRepository guestRepository;
	
	@MockBean
	AddressRepository addressRepository;
	
	@Autowired
	GuestService guestService;
	
	private static Address address = new Address();
	private static Guest guest = new Guest();
	private static Guest guest2 = new Guest();
	private static List<Guest> guestsList = new ArrayList<>();

	@BeforeAll
	public static void beforeAll() {
		
		// setting address to put into the guest paramseters
		address.setAddressName("rua x");
		address.setCity("Amparo");
		address.setCountry("Brasil");
		address.setState("SP");
		address.setZipCode("13900-000");
		
		// setting guest
		guest.setAddress(address);
		guest.setId(14L);
		guest.setBirthday(LocalDate.of(1900, 12, 12));
		guest.setEmail("washington2@orkut.com");
		guest.setName("Washington");
		guest.setLastName("Ferrolho");
		guest.setTitle("MR.");
		guest.setPassword("1234567");
		guest.setRole(Role.ROLE_USER);
		
		// setting guest
		guest2.setAddress(address);
		guest.setId(13L);
		guest2.setBirthday(LocalDate.of(1900, 12, 12));
		guest2.setEmail("francisco@orkut.com");
		guest2.setName("Francisco");
		guest2.setLastName("Neto");
		guest2.setTitle("MR.");
		guest2.setPassword("1234567");
		guest2.setRole(Role.ROLE_USER);
		
		guestsList.add(guest);
		guestsList.add(guest2);
	}

	@Test
	public void shouldReturnAllGuestsWithoutParamAndStatusOk() throws Exception {

		when(guestRepository.findAll()).thenReturn(guestsList);
		
		List<Guest> listAllGuests = guestService.listAllGuests(null, null);
		
		assertEquals(guestsList.size(), listAllGuests.size());
	}
	
	@Test
	public void shouldReturnOneGuestAndStatusOkByParam() throws Exception {

		List<Guest> n = new ArrayList<>();
		n.add(guest2);
		
		when(guestRepository.findByName(guest2.getName())).thenReturn(n);
		
		List<Guest> justOneGuestList = guestService.listAllGuests(guest2.getName(), null);
		
		assertEquals(1, justOneGuestList.size());
		assertEquals(guest2.getLastName(), justOneGuestList.get(0).getLastName());
	}
	
	@Test
	public void shouldReturnEmptyListByUsingNonexistentName() throws Exception {
		
		List<Guest> emptyList = new ArrayList<>();
		
		when(guestRepository.findByName(any())).thenReturn(emptyList);
		
		List<Guest> reqEmptyList = guestService.listAllGuests("nonexistent name", null);
		
		assertEquals(emptyList.size(), reqEmptyList.size());
	}
	
	@Test
	public void shouldReturnOneGuestAndStatusOkByID() throws Exception {

		Optional<Guest> opGuest = Optional.of(guest);
		
		when(guestRepository.findById(guest.getId())).thenReturn(opGuest);

		Guest reqGuest = guestService.listOneGuest(opGuest.get().getId());

		assertEquals(opGuest.get().getName(), reqGuest.getName());
		assertEquals(opGuest.get().getAddress().getCity(), reqGuest.getAddress().getCity());

	}
	
	@Test
	public void shouldThrowExceptionByFindAGuestWithNonexistentID() throws Exception {

		Optional<Guest> opGuest = Optional.empty();
		
		when(guestRepository.findById(guest.getId())).thenReturn(opGuest);

		BaseException thrown = 
				assertThrows(BaseException.class, 
					() -> guestService.listOneGuest(guest.getId()),
					"It was expected that listOneGuest() thrown an exception, " +
					"due to trying to find a guest with an nonexistent ID");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());

	}
	
	@Test
	public void shouldThrowExceptionByFindGuestReservationsWithNonexistentID() throws Exception {
		
		Optional<Guest> opGuest = Optional.empty();
		
		when(guestRepository.findById(guest.getId())).thenReturn(opGuest);
		
		BaseException thrown = 
				assertThrows(BaseException.class, 
						() -> guestService.listGuestReservations(guest.getId()),
						"It was expected that listGuestReservations() thrown an exception, " +
						"due to trying to find a guest reservations with an nonexistent reservation ID");
		
		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
		
	}
	
	@Test
	public void shouldReturnAllGuestReservations() throws BaseException {
		Reservation r1 = Mockito.mock(Reservation.class);
		Reservation r2 = Mockito.mock(Reservation.class);
		Reservation r3 = Mockito.mock(Reservation.class);
		
		Set<Reservation> reservationsList = new HashSet<>();
		
		reservationsList.add(r1);
    	reservationsList.add(r2);
    	reservationsList.add(r3);
    	
    	guest.setReservations(reservationsList);
    	
		when(guestRepository.findById(guest.getId())).thenReturn(Optional.of(guest));
		
		List<Reservation> reqReservationsList = guestService.listGuestReservations(guest.getId());
		
		assertEquals(3, reqReservationsList.size());
	}
	
}
