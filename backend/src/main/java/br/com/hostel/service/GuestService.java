package br.com.hostel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.GuestDto;
import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.GuestForm;
import br.com.hostel.controller.form.GuestUpdateForm;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;

@Service
public class GuestService {
	
	@Autowired
	private GuestRepository guestRepository;

	@Autowired
	private AddressRepository addressRepository;

	public ResponseEntity<?> createGuest(GuestForm form, UriComponentsBuilder uriBuilder) {
		Optional<Guest> guestEmail = guestRepository.findByEmail(form.getEmail());
		
		if(!guestEmail.isPresent()) {
			
			Guest guest = form.returnGuest(addressRepository);
			guestRepository.save(guest);

			URI uri = uriBuilder.path("/guests/{id}").buildAndExpand(guest.getId()).toUri();
			return ResponseEntity.created(uri).body(new GuestDto(guest));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is already a guest with e-mail = " + form.getEmail());
		}
	}

	public ResponseEntity<List<GuestDto>> listAllGuests(String name, Pageable pagination) {
		
		List<GuestDto> response = new ArrayList<>();

		if (name == null)
			response = GuestDto.converter(guestRepository.findAll());
		else 
			response = GuestDto.converter(guestRepository.findByName(name));

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<?> listOneGuest(Long id) {
		
		Optional<Guest> guest = guestRepository.findById(id);
		
		if (guest.isPresent()) return ResponseEntity.ok(new GuestDto(guest.get()));
		
		else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a guest with id = " + id);
	}
	
	public ResponseEntity<?> listGuestReservations(Long guest_ID) {

		Optional<Guest> guest = guestRepository.findById(guest_ID);
		
		if (guest.isPresent()) {
			List<Reservation> reservations = guest.get().getReservations().stream().collect(Collectors.toList());
			
			Collections.sort(reservations);
			
			List<ReservationDto> response = ReservationDto.convert(reservations);
	
			return ResponseEntity.ok(response);
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a guest with id = " + guest_ID);
	}

	public ResponseEntity<?> deleteGuest(Long id) {
		
		Optional<Guest> guest = guestRepository.findById(id);
		
		if (guest.isPresent()) {
			guestRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a guest with id = " + id);
	}

	public ResponseEntity<?> updateGuest(Long id, @Valid GuestUpdateForm form) {
		Optional<Guest> guestOp = guestRepository.findById(id);

		if (guestOp.isPresent()) {
			
			Guest guest = form.updateGuestForm(id, guestOp.get(), guestRepository);
			addressRepository.save(guest.getAddress());

			return ResponseEntity.ok(new GuestDto(guest));
		} else 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a guest with id = " + id);
	}
	
}
