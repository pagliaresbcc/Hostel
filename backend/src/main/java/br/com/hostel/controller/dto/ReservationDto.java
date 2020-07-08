package br.com.hostel.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.hostel.model.Payments;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;

public class ReservationDto {

	private Long id;
	private LocalDate reservationDate;
	private LocalDate checkinDate;
	private LocalDate checkoutDate;
	private int numberOfGuests;
	private Payments payments;
	private Set<Room> rooms;
	
	public ReservationDto() {
	}
	
	public ReservationDto(Reservation reservation) {
		this.id = reservation.getId();
		this.reservationDate = reservation.getReservationDate();
		this.checkinDate =  reservation.getCheckinDate();
		this.checkoutDate = reservation.getCheckoutDate();
		this.payments = reservation.getPayment();
		this.rooms = reservation.getRooms();
		this.numberOfGuests = reservation.getNumberOfGuests();
	}

	public Long getId() {
		return id;
	}

	public LocalDate getReservationDate() {
		return reservationDate;
	}

	public LocalDate getCheckinDate() {
		return checkinDate;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}
	
	public Payments getPayments() {
		return payments;
	}
	
	public Set<Room> getRooms() {
		return rooms;
	}
	
	public int getNumberOfGuests() {
		return numberOfGuests;
	}
	
	public static List<ReservationDto> converter(List<Reservation> reservationsList) {
	
		List<ReservationDto> listCustomersDto = new ArrayList<>();
		for(Reservation r : reservationsList) {
			listCustomersDto.add(new ReservationDto(r));
		}
		
		return listCustomersDto;
	}
}
