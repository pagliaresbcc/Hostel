package br.com.hostel.tests.unit.guest;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
		
		Boolean isExcluded = guestService.deleteGuest(guest.getId());

		assertEquals(false, isExcluded);
	}
	
	@Test
	public void shouldReturnTrueWhenDeletingAGuestWithExistentID() throws Exception {
		
		Optional<Guest> opGuest = Optional.of(guest);
		
		when(guestRepository.findById(any())).thenReturn(opGuest);
		
		Boolean isExcluded = guestService.deleteGuest(guest.getId());
		
		assertEquals(true, isExcluded);
	}
	
}