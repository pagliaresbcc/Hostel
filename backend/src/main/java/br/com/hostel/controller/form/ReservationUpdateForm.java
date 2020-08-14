package br.com.hostel.controller.form;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import br.com.hostel.model.Payments;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;

public class ReservationUpdateForm {
	
	private int numberOfGuests;
	private LocalDate reservationDate;
	private LocalDate checkinDate;
	private LocalDate checkoutDate;
	
	private Set<Room> rooms = new HashSet<>();
	
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

	public Set<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	public Payments getPayment() {
		return payment;
	}

	public void setPayment(Payments payment) {
		this.payment = payment;
	}

	public Reservation updateReservationForm(Long id, Reservation reservation) {
		verifyIfParamIsNotNull(reservation);
		
		return reservation;
	}
	
	private void verifyIfParamIsNotNull(Reservation reservation) {
		
		if (numberOfGuests != 0)
			reservation.setNumberOfGuests(numberOfGuests);
		
		if (reservationDate != null)
			reservation.setReservationDate(reservationDate);
		
		if (checkinDate != null)
			reservation.setCheckinDate(checkinDate);
		
		if (checkoutDate != null)
			reservation.setCheckoutDate(checkoutDate);

		if (rooms != null)
			reservation.setRooms(rooms);
		
		if (payment != null)
			reservation.setPayment(payment);
	}
}
