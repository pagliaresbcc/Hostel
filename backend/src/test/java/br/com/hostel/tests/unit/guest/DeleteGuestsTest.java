package br.com.hostel.tests.unit.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hostel.exceptions.BaseException;
import br.com.hostel.model.Guest;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DeleteGuestsTest {

	@MockBean
	GuestRepository guestRepository;

	@MockBean
	AddressRepository addressRepository;

	@MockBean
	Guest guest;

	@Autowired
	GuestService guestService;

	@Test
	public void shouldReturnFalseWhenDeletingAGuestWithNonExistentID() throws Exception {

		Optional<Guest> nonexistentGuest = Optional.empty();

		when(guestRepository.findById(any())).thenReturn(nonexistentGuest);

		BaseException thrown = 
				assertThrows(BaseException.class, 
					() -> guestService.deleteGuest(guest.getId()),
					"Expected deleteGuest() to throw, but it didn't");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}

	@Test
	public void shouldReturnTrueWhenDeletingAGuestWithExistentID() throws Exception {

		Optional<Guest> opGuest = Optional.of(guest);

		when(guestRepository.findById(any())).thenReturn(opGuest);

		guestService.deleteGuest(guest.getId());
	}

}