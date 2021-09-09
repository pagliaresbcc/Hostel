package br.com.hostel.tests.unit.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hostel.exceptions.BaseException;
import br.com.hostel.initializer.GuestsInitializer;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GuestService.class)
public class ListGuestsTest {

	@MockBean
	private GuestRepository guestRepository;
	
	@MockBean
	private AddressRepository addressRepository;
	
	@Autowired
	private GuestService guestService;
	
	private static Address address = new Address();
	private static Guest guest = new Guest();
	private static Guest guest2 = new Guest();
	private static List<Guest> guestsList = new ArrayList<>();

	@BeforeAll
	public static void beforeAll() throws Exception {
		
		GuestsInitializer.initialize(address, guest);
		GuestsInitializer.initialize(address, guest2);

		// setting different attributes to second guest
		guest.setId(13L);
		guest2.setEmail("francisco@orkut.com");
		guest2.setName("Francisco");
		guest2.setLastName("Neto");
		guest2.setTitle("MR.");
		
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
}
