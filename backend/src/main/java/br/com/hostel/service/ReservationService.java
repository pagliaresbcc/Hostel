package br.com.hostel.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.controller.form.ReservationUpdateForm;
import br.com.hostel.exceptions.reservation.ReservationException;
import br.com.hostel.exceptions.room.RoomException;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.repository.GuestRepository;
import br.com.hostel.repository.PaymentRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private GuestRepository guestRepository;

	public Reservation registerReservation(ReservationForm form, UriComponentsBuilder uriBuilder) throws RoomException {

		Reservation reservation = form.returnReservation(paymentRepository, roomRepository);
		Optional<Guest> guestOp = guestRepository.findById(reservation.getGuest_ID());

		if (!guestOp.isPresent())
			throw new ReservationException("Guest ID haven't found", HttpStatus.NOT_FOUND);

		if (reservation.getRooms().isEmpty())
			throw new ReservationException("Rooms list cannot be empty", HttpStatus.BAD_REQUEST);

		if (reservation.getCheckinDate().isBefore(LocalDate.now())
				|| reservation.getCheckoutDate().isBefore(reservation.getCheckinDate()))
			throw new ReservationException("Verify your checkin/checkout date", HttpStatus.BAD_REQUEST);

		Guest guest = guestOp.get();

		reservation.setGuestName(guest.getName());
		
		paymentRepository.save(reservation.getPayment());
		reservationRepository.save(reservation);

		guest.addReservation(reservation);
		guestRepository.save(guest);

		return reservation;
	}

	public List<Reservation> listAllReservations(Long guestId, Pageable pagination) {

		List<Reservation> response = new ArrayList<>();

		if (guestId == null)
			response = reservationRepository.findAll();
		else {
			Optional<Guest> guest = guestRepository.findById(guestId);

			if (guest.isPresent()) {

				response = guest.get().getReservations().stream().collect(Collectors.toList());
			}
		}

		return response;
	}

	public Reservation listOneReservation(Long id) throws RoomException {

		Optional<Reservation> reservation = reservationRepository.findById(id);

		if (!reservation.isPresent())
			throw new ReservationException("There isn't a reservation with id = " + id, HttpStatus.NOT_FOUND);

		return reservation.get();

	}

	public Reservation updateReservation(@PathVariable Long id, @RequestBody @Valid ReservationUpdateForm form)
			throws RoomException {

		Optional<Reservation> reservationOp = reservationRepository.findById(id);

		if (!reservationOp.isPresent())
			throw new ReservationException("There isn't a reservation with id = " + id, HttpStatus.NOT_FOUND);

		Reservation reservation = form.updateReservationForm(id, reservationOp.get(), paymentRepository,
				roomRepository);

		if (reservation.getRooms().isEmpty())
			throw new ReservationException("Reservation rooms list cannot be empty", HttpStatus.BAD_REQUEST);

		reservation.getRooms().forEach(room -> roomRepository.save(room));

		paymentRepository.save(reservation.getPayment());

		return reservation;
	}

	public void deleteReservation(Long id) throws RoomException {

		Optional<Reservation> reservation = reservationRepository.findById(id);

		if (!reservation.isPresent())
			throw new ReservationException("There isn't a reservation with id = " + id, HttpStatus.NOT_FOUND);
			
		reservationRepository.deleteById(id);

	}
}
