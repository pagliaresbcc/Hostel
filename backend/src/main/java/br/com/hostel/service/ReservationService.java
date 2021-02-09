package br.com.hostel.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.controller.form.ReservationUpdateForm;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.repository.PaymentsRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private GuestRepository guestRepository;

	public ResponseEntity<?> registerReservation(ReservationForm form, UriComponentsBuilder uriBuilder) {
		
		Reservation reservation = form.returnReservation(paymentsRepository, roomRepository);
		Optional<Guest> guestOp = guestRepository.findById(reservation.getGuest_ID());

		if (guestOp.isPresent()) {
			
			if (reservation.getRooms().isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rooms list cannot be empty");
			
			else if (reservation.getCheckinDate().isAfter(LocalDate.now())
					&& reservation.getCheckoutDate().isAfter(reservation.getCheckinDate())) {
				
				Guest guest = guestOp.get();

				paymentsRepository.save(reservation.getPayment());
				reservationRepository.save(reservation);
				
				guest.addReservation(reservation);
				guestRepository.save(guest);

				URI uri = uriBuilder.path("/reservations/{id}").buildAndExpand(guest.getId()).toUri();

				return ResponseEntity.created(uri).body(new ReservationDto(reservation));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verify your checkin/checkout date");
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guest ID haven't found");
	}

	public ResponseEntity<List<ReservationDto>> listAllReservations(String name, Pageable pagination) {

		List<ReservationDto> response = new ArrayList<>();

		if (name == null)
			response = ReservationDto.convert(reservationRepository.findAll());
		else {
			
			List<Guest> guestList = guestRepository.findByName(name);
			
			if(guestList.size() > 0) {

				List<Reservation> reservations = guestList.get(0).getReservations().stream()
						.collect(Collectors.toList());
		
				response = ReservationDto.convert(reservations);
			}
		}

		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<?> listOneReservation(Long id) {
		
		Optional<Reservation> reservation = reservationRepository.findById(id);
		
		if (reservation.isPresent()) return ResponseEntity.ok(new ReservationDto(reservation.get()));
		
		else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a reservation with id = " + id);
	}

	public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody @Valid ReservationUpdateForm form) {

		Optional<Reservation> reservationOp = reservationRepository.findById(id);

		if (reservationOp.isPresent()) {

			Reservation reservation = form.updateReservationForm(id, reservationOp.get(), paymentsRepository,
					roomRepository);

			if (reservation.getRooms().isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reservation rooms list cannot be empty");
			else
				reservation.getRooms().forEach(room -> roomRepository.save(room));

			paymentsRepository.save(reservation.getPayment());

			return ResponseEntity.ok(new ReservationDto(reservation));
		} else 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a reservation with id = " + id);
	}
	
	public ResponseEntity<?> deleteRoomsReservation(Long id) {
		
		Optional<Reservation> reservation = reservationRepository.findById(id);

		if (reservation.isPresent()) {
			
			reservation.get().setRooms(null);
			reservationRepository.save(reservation.get());
			
			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a reservation with id = " + id);
	}

	public ResponseEntity<?> deleteReservation(Long id) {

		Optional<Reservation> reservation = reservationRepository.findById(id);

		if (reservation.isPresent()) {
			reservationRepository.deleteById(id);

			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a reservation with id = " + id);
	}
}
