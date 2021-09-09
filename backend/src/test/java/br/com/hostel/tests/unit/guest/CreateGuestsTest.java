package br.com.hostel.tests.unit.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.form.GuestForm;
import br.com.hostel.exceptions.BaseException;
import br.com.hostel.initializer.GuestsInitializer;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GuestService.class)
public class CreateGuestsTest {

	@MockBean
	private GuestRepository guestRepository;
	
	@MockBean
	private AddressRepository addressRepository;
	
	@MockBean
	private GuestForm guestForm;
	
	@MockBean
	private UriComponentsBuilder uriBuilder;
	
	@Autowired
	private GuestService guestService;
	
	private static Address address = new Address();
	private static Guest guest = new Guest();

	@BeforeAll
	public static void beforeAll() throws Exception {
		
		GuestsInitializer.initialize(address, guest);
	}

	@Test
	public void shouldCreateAGuestAndReturn() throws Exception {
		
		Optional<Guest> nonexistentGuest = Optional.empty();

		when(guestRepository.findByEmail(any())).thenReturn(nonexistentGuest);
		when(guestForm.returnGuest(any())).thenReturn(guest);
		when(guestRepository.save(any())).thenReturn(guest);
		
		Guest reqGuest = guestService.createGuest(guestForm, uriBuilder);
		
		assertEquals(guest.getName(), reqGuest.getName());
		assertEquals(guest.getLastName(), reqGuest.getLastName());
	}
	
	@Test
	public void shouldReturnNullWithExistentGuestEmail() throws Exception {
		
		Optional<Guest> opGuest = Optional.of(guest);
		
		when(guestRepository.findByEmail(any())).thenReturn(opGuest);
		
		BaseException thrown = 
				assertThrows(BaseException.class, 
					() -> guestService.createGuest(guestForm, uriBuilder),
					"It was expected that createGuest() thrown an exception, " +
					"due to trying to create a guest with an existent email");

		assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
		
	}
}
