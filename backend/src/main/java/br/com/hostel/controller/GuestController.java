package br.com.hostel.controller;

import br.com.hostel.controller.dto.GuestDto;
import br.com.hostel.controller.form.GuestForm;
import br.com.hostel.controller.form.GuestUpdateForm;
import br.com.hostel.model.Guest;
import br.com.hostel.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

	@Autowired
	private GuestService guestService;

	@PostMapping
	public ResponseEntity<GuestDto> createGuest(@RequestBody @Valid GuestForm form, UriComponentsBuilder uriBuilder) {

		Guest guest = guestService.createGuest(form);

		URI uri = uriBuilder.path("/guests/{id}").buildAndExpand(guest.getId()).toUri();

		return ResponseEntity.created(uri).body(new GuestDto(guest));
	}

	@GetMapping
	public ResponseEntity<List<GuestDto>> listAllGuests(@RequestParam(required = false) String name) {

		List<Guest> response = guestService.listAllGuests(name);

		return ResponseEntity.ok(GuestDto.converter(response));

	}

	@GetMapping("/{id}")
	public ResponseEntity<GuestDto> listOneGuest(@PathVariable Long id) {

		Guest guest = guestService.listOneGuest(id);

		return ResponseEntity.ok(new GuestDto(guest));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<GuestDto> updateGuest(@PathVariable Long id, @RequestBody @Valid GuestUpdateForm form) {

		Guest guest = guestService.updateGuest(id, form);

		return ResponseEntity.ok(new GuestDto(guest));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Object> deleteGuest(@PathVariable Long id) {

		guestService.deleteGuest(id);

		return ResponseEntity.ok().build();
	}
}
