package br.com.hostel.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import br.com.hostel.controller.form.GuestForm;
import br.com.hostel.controller.form.GuestUpdateForm;
import br.com.hostel.model.Guest;
import br.com.hostel.service.GuestService;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

	@Autowired
	private GuestService guestService;

	@PostMapping
	public ResponseEntity<?> createGuest(@RequestBody @Valid GuestForm form, UriComponentsBuilder uriBuilder) {

		Guest guest = guestService.createGuest(form, uriBuilder);

		URI uri = uriBuilder.path("/guests/{id}").buildAndExpand(guest.getId()).toUri();

		return ResponseEntity.created(uri).body(new GuestDto(guest));
	}

	@GetMapping
	public ResponseEntity<List<GuestDto>> listAllGuests(@RequestParam(required = false) String name,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable pagination)
			throws URISyntaxException {

		List<Guest> response = guestService.listAllGuests(name, pagination);

		return ResponseEntity.ok(GuestDto.converter(response));

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> listOneGuest(@PathVariable Long id) {

		Guest guest = guestService.listOneGuest(id);

		return ResponseEntity.ok(new GuestDto(guest));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateGuest(@PathVariable Long id, @RequestBody @Valid GuestUpdateForm form,
			UriComponentsBuilder uriBuilder) {

		Guest guest = guestService.updateGuest(id, form);

		return ResponseEntity.ok(new GuestDto(guest));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteGuest(@PathVariable Long id) {

		guestService.deleteGuest(id);

		return ResponseEntity.ok().build();
	}
}
