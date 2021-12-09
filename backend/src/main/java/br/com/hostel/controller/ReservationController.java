package br.com.hostel.controller;

import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.controller.form.ReservationUpdateForm;
import br.com.hostel.model.Reservation;
import br.com.hostel.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;

	@PostMapping
	public ResponseEntity<ReservationDto> registerReservation(@RequestBody @Valid ReservationForm form,
			UriComponentsBuilder uriBuilder) {

		Reservation reservation = reservationService.registerReservation(form);
		
		URI uri = uriBuilder.path("/reservations/{id}").buildAndExpand(form.getGuest_ID()).toUri();

		return ResponseEntity.created(uri).body(new ReservationDto(reservation));
	}

	@GetMapping
	public ResponseEntity<List<ReservationDto>> listAllReservations(@RequestParam(required = false) Long guestId) {

		List<Reservation> reservationsList = reservationService.listAllReservations(guestId);
		
		return ResponseEntity.ok(ReservationDto.convert(reservationsList));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReservationDto> listOneReservation(@PathVariable Long id) {

		Reservation reservation = reservationService.listOneReservation(id);
		
		return ResponseEntity.ok(new ReservationDto(reservation));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ReservationDto> updateReservation(@PathVariable Long id,
			@RequestBody @Valid ReservationUpdateForm form) {

		Reservation reservation = reservationService.updateReservation(id, form);
		
		return ResponseEntity.ok(new ReservationDto(reservation));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Object> deleteReservation(@PathVariable Long id) {

		reservationService.deleteReservation(id);
		
		return ResponseEntity.ok().build();
	}
}
