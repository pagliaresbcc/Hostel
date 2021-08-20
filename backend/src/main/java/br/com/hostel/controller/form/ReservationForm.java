package br.com.hostel.controller.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import br.com.hostel.model.Payment;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import br.com.hostel.repository.PaymentRepository;
import br.com.hostel.repository.RoomRepository;

public class ReservationForm {

	@NotNull
	private Long guest_ID;
	@NotNull
	private String guestName;
	@NotNull
    private int numberOfGuests;
	@NotNull
	private LocalDate checkinDate;
	@NotNull
	private LocalDate checkoutDate;
	@NotNull
	private List<Long> rooms_ID = new ArrayList<>();
	@NotNull
	private Payment payment;

	public Long getGuest_ID() {
		return guest_ID;
	}
	
	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	
	public void setGuest_ID(Long guest_ID) {
		this.guest_ID = guest_ID;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
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

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	

	public Reservation returnReservation(PaymentRepository paymentRepository, RoomRepository roomRepository) {
		
		payment.setDate(LocalDateTime.now());
		
		Set<Room> roomsList = new HashSet<>();

		rooms_ID.forEach(id -> roomsList.add(roomRepository.findById(id).get()));

		return new Reservation(guest_ID, guestName, numberOfGuests, LocalDate.now(), checkinDate, checkoutDate, 
				roomsList, payment);
	}

}
