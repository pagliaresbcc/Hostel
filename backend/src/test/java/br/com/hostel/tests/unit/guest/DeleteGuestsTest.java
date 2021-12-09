package br.com.hostel.tests.unit.guest;

import br.com.hostel.exceptions.guest.GuestException;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DeleteGuestsTest {

	@MockBean
	private GuestRepository guestRepository;

	@MockBean
	private Guest guest;

	@Autowired
	private GuestService guestService;

	@Test
	public void shouldReturnNotFoundStatusAfterTryingToDeleteAGuestWithNonExistentID() {

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
	public void shouldAssertFalseAfterDeletingAGuestAndTryingToFindHim() {

		Optional<Guest> opGuest = Optional.of(guest);

		when(guestRepository.findById(any())).thenReturn(opGuest).thenReturn(Optional.empty());
		
		guestService.deleteGuest(guest.getId());
		
		GuestException thrown = 
				assertThrows(GuestException.class,
					() -> guestService.listOneGuest(guest.getId()),
					"It was expected that deleteGuest() thrown an exception, "+
					"due to trying to delete a guest with an nonexistent ID");
		
		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
		assertEquals("There isn't a guest with id = " + guest.getId(), thrown.getMessage());
	}

}
