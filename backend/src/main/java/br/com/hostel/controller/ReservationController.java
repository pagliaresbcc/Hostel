package br.com.hostel.controller;

import java.net.URI;
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
import br.com.hostel.exceptions.BaseException;
import br.com.hostel.model.Reservation;
import br.com.hostel.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;

	@PostMapping
	public ResponseEntity<?> registerReservation(@RequestBody @Valid ReservationForm form,
			UriComponentsBuilder uriBuilder) {

		try {
			Reservation reservation = reservationService.registerReservation(form, uriBuilder);
			
			URI uri = uriBuilder.path("/reservations/{id}").buildAndExpand(form.getGuest_ID()).toUri();
	
			return ResponseEntity.created(uri).body(new ReservationDto(reservation));
		} catch(BaseException be) {
			return ResponseEntity.status(be.getStatusCode()).body(be.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<List<ReservationDto>> listAllReservations(@RequestParam(required = false) String name,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable pagination) {

		List<Reservation> reservationsList = reservationService.listAllReservations(name, pagination);
		
		return ResponseEntity.ok(ReservationDto.convert(reservationsList));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> listOneReservation(@PathVariable Long id) {

		try {
			Reservation reservation = reservationService.listOneReservation(id);
			
			return ResponseEntity.ok(new ReservationDto(reservation));
		} catch(BaseException be) {
			return ResponseEntity.status(be.getStatusCode()).body(be.getMessage());
		}
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateReservation(@PathVariable Long id,
			@RequestBody @Valid ReservationUpdateForm form, UriComponentsBuilder uriBuilder) {

		try {
			Reservation reservation = reservationService.updateReservation(id, form);
			
			return ResponseEntity.ok(new ReservationDto(reservation));
		} catch(BaseException be) {
			return ResponseEntity.status(be.getStatusCode()).body(be.getMessage());
		}
	}
	
	@DeleteMapping("/deleteRoomsReservation/{id}")
	@Transactional
	public ResponseEntity<?> deleteRoomsReservation(@PathVariable Long id) {
		
		try {
			reservationService.deleteRoomsReservation(id);
			
			return ResponseEntity.ok().build();
		} catch(BaseException be) {
			return ResponseEntity.status(be.getStatusCode()).body(be.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteReservation(@PathVariable Long id) {

		try {
			reservationService.deleteReservation(id);
			
			return ResponseEntity.ok().build();
		} catch(BaseException be) {
			return ResponseEntity.status(be.getStatusCode()).body(be.getMessage());
		}
	}
}
