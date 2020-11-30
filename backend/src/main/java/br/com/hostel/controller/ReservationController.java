package br.com.hostel.controller;

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

import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.controller.form.ReservationUpdateForm;
import br.com.hostel.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;

	@PostMapping
	public ResponseEntity<?> registerReservation(@RequestBody @Valid ReservationForm form,
			UriComponentsBuilder uriBuilder) {

		return this.reservationService.registerReservation(form, uriBuilder);
	}

	@GetMapping
	public ResponseEntity<List<ReservationDto>> listAllReservations(@RequestParam(required = false) String name,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable pagination) {

		return this.reservationService.listAllReservations(name, pagination);
	}
	

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateReservation(@PathVariable Long id,
			@RequestBody @Valid ReservationUpdateForm form, UriComponentsBuilder uriBuilder) {

		return this.reservationService.updateReservation(id, form);
	}
	
	@DeleteMapping("/deleteRoomsReservation/{id}")
	@Transactional
	public ResponseEntity<?> deleteRoomsReservation(@PathVariable Long id) {
		return this.reservationService.deleteRoomsReservation(id);

	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteReservation(@PathVariable Long id) {

		return this.reservationService.deleteReservation(id);
	}
}
