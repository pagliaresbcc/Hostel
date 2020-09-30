package br.com.hostel.controller.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.hostel.model.Payments;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import br.com.hostel.repository.PaymentsRepository;
import br.com.hostel.repository.RoomRepository;

public class ReservationUpdateForm {
	
	private int numberOfGuests;
	private LocalDate reservationDate;
	private LocalDate checkinDate;
	private LocalDate checkoutDate;
	
	private List<Long> rooms_ID = new ArrayList<>();	
	
	private Payments payment;
	
	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public LocalDate getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}

	public LocalDate getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(LocalDate checkinDate) {
		this.checkinDate = checkinDate;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public List<Long> getRooms_ID() {
		return rooms_ID;
	}

	public void setRooms_ID(List<Long> rooms_ID) {
		this.rooms_ID = rooms_ID;
	}

	public Payments getPayment() {
		return payment;
	}

	public void setPayment(Payments payment) {
		this.payment = payment;
	}

	public Reservation updateReservationForm(Long id, Reservation reservation, 
			PaymentsRepository paymentsRepository, RoomRepository roomRepository) {
		verifyIfParamIsNotNull(reservation, roomRepository);
		
		return reservation;
	}
	
	private void verifyIfParamIsNotNull(Reservation reservation, RoomRepository roomRepository) {
		
		if (numberOfGuests != 0)
			reservation.setNumberOfGuests(numberOfGuests);
		
		if (reservationDate != null)
			reservation.setReservationDate(reservationDate);
		
		if (checkinDate != null)
			reservation.setCheckinDate(checkinDate);
		
		if (checkoutDate != null)
			reservation.setCheckoutDate(checkoutDate);

		if (rooms_ID != null) {
			Set<Room> roomsList = new HashSet<>();

			rooms_ID.forEach(id -> roomsList.add(roomRepository.findById(id).get()));

			reservation.setRooms(roomsList);
		}
			
		
		if (payment != null) {
			payment.setDate(LocalDateTime.now());
			reservation.setPayment(payment);
		}
	}
}
