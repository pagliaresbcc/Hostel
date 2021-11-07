package br.com.hostel.tests.unit.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hostel.exceptions.guest.GuestException;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DeleteGuestsTest {

	@MockBean
	private GuestRepository guestRepository;

	@MockBean
	private AddressRepository addressRepository;

	@MockBean
	private Guest guest;

	@Autowired
	private GuestService guestService;

	@Test
	public void shouldReturnNotFoundStatusAfterTryingToDeleteAGuestWithNonExistentID() throws Exception {

		Optional<Guest> nonexistentGuest = Optional.empty();

		when(guestRepository.findById(any())).thenReturn(nonexistentGuest);

		GuestException thrown = 
				assertThrows(GuestException.class, 
					() -> guestService.deleteGuest(guest.getId()),
					"It was expected that deleteGuest() thrown an exception, "+
					"due to trying to delete a guest with an nonexistent ID");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}

	@Test
	public void shouldAssertFalseAfterDeletingAGuestAndTryingToFindHim() throws Exception {

		Optional<Guest> opGuest = Optional.of(guest);

		when(guestRepository.findById(any())).thenReturn(opGuest).thenReturn(Optional.empty());
		
		guestService.deleteGuest(guest.getId());
		
		Optional<Guest> findByIdResponse = guestRepository.findById(guest.getId());
		
		assertFalse(findByIdResponse.isPresent());
	}

}
