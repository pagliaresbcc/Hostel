package br.com.hostel.tests.unit.guest;

import br.com.hostel.controller.form.GuestForm;
import br.com.hostel.exceptions.guest.GuestException;
import br.com.hostel.initializer.GuestsInitializer;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GuestService.class)
public class CreateGuestsTest {

	@MockBean
	private GuestRepository guestRepository;

	@MockBean
	private AddressRepository addressRepository;

	@MockBean
	private GuestForm guestForm;

	@Autowired
	private GuestService guestService;
	
	private static Address address = new Address();
	private static Guest guest = new Guest();

	@BeforeAll
	public static void beforeAll() throws Exception {
		
		GuestsInitializer.initialize(address, guest);
	}

	@Test
	public void shouldCreateOneGuestSuccessfully() {
		
		Optional<Guest> nonexistentGuest = Optional.empty();

		when(guestRepository.findByEmail(any())).thenReturn(nonexistentGuest);
		when(guestForm.returnGuest(any())).thenReturn(guest);
		when(guestRepository.save(any())).thenReturn(guest);
		
		Guest reqGuest = guestService.createGuest(guestForm);
		
		assertEquals(guest.getName(), reqGuest.getName());
		assertEquals(guest.getLastName(), reqGuest.getLastName());
	}
	
	@Test
	public void shouldReturnNullWithExistentGuestEmail() {
		
		Optional<Guest> opGuest = Optional.of(guest);
		
		when(guestRepository.findByEmail(any())).thenReturn(opGuest);
		
		GuestException thrown = 
				assertThrows(GuestException.class, 
					() -> guestService.createGuest(guestForm),
					"It was expected that createGuest() thrown an exception, " +
					"due to trying to create a guest with an existent email");

		assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
		
	}
}
