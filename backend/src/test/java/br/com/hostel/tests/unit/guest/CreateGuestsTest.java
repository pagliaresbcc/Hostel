package br.com.hostel.tests.unit.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.helper.Role;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.service.GuestService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GuestService.class)
public class CreateGuestsTest {

	@MockBean
	GuestRepository guestRepository;
	
	@MockBean
	AddressRepository addressRepository;
	
	@MockBean
	GuestForm guestForm;
	
	@MockBean
	UriComponentsBuilder uriBuilder;
	
	@Autowired
	GuestService guestService;
	
	private static Address address = new Address();
	private static Guest guest = new Guest();

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
		guest.setId(13L);
		guest.setBirthday(LocalDate.of(1900, 12, 12));
		guest.setEmail("washington2@orkut.com");
		guest.setName("Washington");
		guest.setLastName("Ferrolho");
		guest.setTitle("MR.");
		guest.setPassword("1234567");
		guest.setRole(Role.ROLE_USER);
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
