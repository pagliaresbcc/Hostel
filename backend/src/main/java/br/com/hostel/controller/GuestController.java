package br.com.hostel.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.GuestDto;
import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.GuestForm;
import br.com.hostel.controller.form.GuestUpdateForm;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.service.GuestService;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

	@Autowired
	private GuestService guestService;

	@PostMapping
	public ResponseEntity<?> createGuest(@RequestBody @Valid GuestForm form, UriComponentsBuilder uriBuilder) {

		Guest guest = guestService.createGuest(form, uriBuilder);

		if (guest == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("There is already a guest with e-mail = " + form.getEmail());

		URI uri = uriBuilder.path("/guests/{id}").buildAndExpand(guest.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new GuestDto(guest));
	}

	@GetMapping
	public ResponseEntity<List<GuestDto>> listAllGuests(@RequestParam(required = false) String name,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable pagination)
			throws URISyntaxException {

		List<GuestDto> response = new ArrayList<>();

		response = GuestDto.converter(guestService.listAllGuests(name, pagination));

		return ResponseEntity.ok(response);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> listOneGuest(@PathVariable Long id) {

		Guest guest = guestService.listOneGuest(id);

		if (guest == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a guest with id = " + id);

		return ResponseEntity.ok(new GuestDto(guest));

	}

	@GetMapping("/{id}/reservations")
	public ResponseEntity<?> listGuestReservations(@PathVariable Long id) {
		List<Reservation> reservations = guestService.listGuestReservations(id);

		if (reservations == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a guest with id = " + id);

		List<ReservationDto> response = ReservationDto.convert(reservations);

		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateGuest(@PathVariable Long id, @RequestBody @Valid GuestUpdateForm form,
			UriComponentsBuilder uriBuilder) {

		Guest guest = guestService.updateGuest(id, form);
		
		if (guest == null) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a guest with id = " + id);
			
		return ResponseEntity.ok(new GuestDto(guest));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteGuest(@PathVariable Long id) {

		Boolean isExcluded = guestService.deleteGuest(id);
		
		if (!isExcluded) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a guest with id = " + id);
		
		return ResponseEntity.ok().build();
	}
}
