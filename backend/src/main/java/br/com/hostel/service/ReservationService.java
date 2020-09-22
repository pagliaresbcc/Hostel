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
import br.com.hostel.model.Customer;
import br.com.hostel.model.Reservation;
import br.com.hostel.repository.CustomerRepository;
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
	private CustomerRepository customerRepository;

	public ResponseEntity<?> registerReservation(ReservationForm form, UriComponentsBuilder uriBuilder) {
		Reservation reservation = form.returnReservation(paymentsRepository, roomRepository);
		Optional<Customer> customerOp = customerRepository.findById(reservation.getCustomer_ID());

		if (customerOp.isPresent()) {
			if (reservation.getRooms().isEmpty()) 
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rooms list cannot be empty");
			else if (reservation.getCheckinDate().isAfter(LocalDate.now())
					&& reservation.getCheckoutDate().isAfter(reservation.getCheckinDate())) {
				Customer customer = customerOp.get();

				reservationRepository.save(reservation);
				customer.addReservation(reservation);
				customerRepository.save(customer);

				URI uri = uriBuilder.path("/reservations/{id}").buildAndExpand(customer.getId()).toUri();

				return ResponseEntity.created(uri).body(new ReservationDto(reservation));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verify your checkin/checkout date");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer ID didn't found");
	}

	public ResponseEntity<List<ReservationDto>> listAllReservations(String name, Pageable pagination) {

		List<ReservationDto> response = new ArrayList<>();

		if (name == null)
			response = ReservationDto.converter(reservationRepository.findAll());
		else {
			List<Customer> customerList = customerRepository.findByName(name);
			
			List<Reservation> reservations = customerList.get(0).getReservations().stream()
						.collect(Collectors.toList());

			response = ReservationDto.converter(reservations);
		}

		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<List<ReservationDto>> listCustomerReservations(Long customer_ID) {
		
		Optional<Customer> customer = customerRepository.findById(customer_ID);
		
		List<ReservationDto> response = new ArrayList<>();
		
		List<Reservation> reservations = customer.get().getReservations().stream()
				.collect(Collectors.toList());

		response = ReservationDto.converter(reservations);
		
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<?> updateReservation(@PathVariable Long id,
			@RequestBody @Valid ReservationUpdateForm form, UriComponentsBuilder uriBuilder) {

		Optional<Reservation> reservationOp = reservationRepository.findById(id);

		if (reservationOp.isPresent()) {
			Reservation reservation = form.updateReservationForm(id, reservationOp.get());

			reservation.getRooms().forEach(room -> roomRepository.save(room));

			paymentsRepository.save(reservation.getPayment());

			return ResponseEntity.ok(new ReservationDto(reservation));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There isn't a reservation with that ID");
	}

	public ResponseEntity<?> deleteReservation(Long id) {
		
		Optional<Reservation> reservation = reservationRepository.findById(id);

		if (reservation.isPresent()) {
			reservationRepository.deleteById(id);

			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There isn't a reservation with that ID");
	}

}
