package br.com.hostel.tests.unit.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.com.hostel.controller.form.GuestUpdateForm;
import br.com.hostel.exceptions.BaseException;
import br.com.hostel.initializer.GuestsInitializer;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GuestService.class)
public class UpdateGuestsTest {

	@MockBean
	private GuestRepository guestRepository;
	
	@MockBean
	private AddressRepository addressRepository;
	
	@MockBean
	private GuestUpdateForm guestUpdateForm;
	
	@Autowired
	private GuestService guestService;
	
	private static Address address = new Address();
	private static Guest guest = new Guest();
	
	@BeforeAll
	public static void beforeAll() throws Exception {
		
		GuestsInitializer.initialize(address, guest);
	}

	@Test
	public void shouldUpdateGuestNameAndLastName() throws Exception {

		Optional<Guest> opGuest = Optional.of(guest);
		
		opGuest.get().setName("Francisco");
		opGuest.get().setLastName("Neto");

		when(guestRepository.findById(guest.getId())).thenReturn(opGuest);
		when(guestUpdateForm.updateGuestForm(guest.getId(), guest, guestRepository)).thenReturn(guest);
		when(addressRepository.save(guest.getAddress())).thenReturn(address);
		
		Guest reqGuest = guestService.updateGuest(guest.getId(), guestUpdateForm);
		
		assertEquals(opGuest.get().getName(), reqGuest.getName());
		assertEquals(opGuest.get().getLastName(), reqGuest.getLastName());
	}
	
	@Test
	public void shouldNotUpdateGuestWithNonexistenteID() throws Exception {
		
		Optional<Guest> nonexistentGuest = Optional.empty();
		
		when(guestRepository.findById(guest.getId())).thenReturn(nonexistentGuest);
		
		BaseException thrown = 
				assertThrows(BaseException.class, 
					() -> guestService.updateGuest(guest.getId(), guestUpdateForm),
					"it was expected that updateGuest() thrown an exception, " +
					"due to trying to update a guest with an nonexistent ID");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}
}
