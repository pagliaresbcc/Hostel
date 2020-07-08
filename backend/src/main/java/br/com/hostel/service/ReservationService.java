package br.com.hostel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.ReservationForm;
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

	public ResponseEntity<ReservationDto> registerReservation(ReservationForm form, UriComponentsBuilder uriBuilder) {

		Reservation reservation = form.returnReservation(paymentsRepository, roomRepository);
//		System.out.println(reservation.getCustomer_ID()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//		if(customerRepository.findById(reservation.getCustomer_ID()).isPresent()) {
		reservationRepository.save(reservation);
		Customer customer = customerRepository.findById(reservation.getCustomer_ID()).get();
		customer.addReservation(reservation);
		customerRepository.save(customer);
		URI uri = uriBuilder.path("/reservations/{id}").buildAndExpand(customer.getId()).toUri();
		return ResponseEntity.created(uri).body(new ReservationDto(reservation));
//		} else
//			return ResponseEntity.badRequest().build();
	}

	public ResponseEntity<List<ReservationDto>> listAllReservations(String name, Pageable pagination) {

		List<ReservationDto> response = new ArrayList<>();

		if (name == null)
			response = ReservationDto.converter(reservationRepository.findAll());
		else {
			List<Customer> customerList = customerRepository.findByName(name);
			if (!customerList.isEmpty()) {
				List<Reservation> reservations = customerList.get(0).getReservations().stream()
						.collect(Collectors.toList());

				response = ReservationDto.converter(reservations);
			}
		}

		if (response.isEmpty())
			return ResponseEntity.notFound().build();
		else
			return ResponseEntity.ok(response);
	}

	public ResponseEntity<ReservationDto> listOneReservation(Long id) {
		Optional<Reservation> reservation = reservationRepository.findById(id);

		if (reservation.isPresent())
			return ResponseEntity.ok(new ReservationDto(reservation.get()));
		else
			return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> deleteReservation(Long id) {
		Optional<Reservation> reservation = reservationRepository.findById(id);

		if (reservation.isPresent()) {
			reservationRepository.deleteById(id);

			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.notFound().build();
	}

}
