package br.com.hostel.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import br.com.hostel.model.Payment;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDto {

	private Long id;
	private String guestName;
	private LocalDate reservationDate;
	private LocalDate checkinDate;
	private LocalDate checkoutDate;
	private int numberOfGuests;
	private Payment payment;
	private Set<Room> rooms;

	public ReservationDto(Reservation reservation) {
		this.id = reservation.getId();
		this.guestName = reservation.getGuestName();
		this.reservationDate = reservation.getReservationDate();
		this.checkinDate =  reservation.getCheckinDate();
		this.checkoutDate = reservation.getCheckoutDate();
		this.payment = reservation.getPayment();
		this.rooms = reservation.getRooms();
		this.numberOfGuests = reservation.getNumberOfGuests();
	}

	public static List<ReservationDto> convert(List<Reservation> reservationsList) {
	
		List<ReservationDto> reservationDtoList = new ArrayList<>();
		
		Collections.sort(reservationsList);
		
		reservationsList.forEach(reservation -> reservationDtoList.add(new ReservationDto(reservation)));
		
		return reservationDtoList;
	}
}
