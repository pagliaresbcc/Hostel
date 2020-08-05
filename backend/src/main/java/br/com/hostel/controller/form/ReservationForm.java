package br.com.hostel.controller.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import br.com.hostel.model.Payments;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import br.com.hostel.repository.PaymentsRepository;
import br.com.hostel.repository.RoomRepository;

public class ReservationForm {

	@NotNull
	private Long customer_ID;
	@NotNull
    int numberOfGuests;
	@NotNull
	LocalDate reservationDate;
	@NotNull
	LocalDate checkinDate;
	@NotNull
	LocalDate checkoutDate;
	@NotNull
	private List<Long> rooms_ID = new ArrayList<>();
	@NotNull
	private Payments payment;

	public Long getCustomer_ID() {
		return customer_ID;
	}

	public void setCustomer_ID(Long customer_ID) {
		this.customer_ID = customer_ID;
	}

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
	

	public Reservation returnReservation(PaymentsRepository paymentsRepository, RoomRepository roomRepository) {
		paymentsRepository.save(payment);
		Set<Room> roomsList = new HashSet<>();

		rooms_ID.forEach(id -> roomsList.add(roomRepository.findById(id).get()));

		return new Reservation(customer_ID, numberOfGuests, reservationDate, checkinDate, checkoutDate, 
				roomsList, payment);
	}

}
