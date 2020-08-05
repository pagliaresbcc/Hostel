package br.com.hostel.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class CheckinCheckoutDates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private LocalDate checkIn;
	@NotNull
	private LocalDate checkOut;

	public CheckinCheckoutDates() {

	}

	public CheckinCheckoutDates(LocalDate checkIn, LocalDate checkOut) {
		this.setCheckIn(checkIn);
		this.setCheckOut(checkOut);
	}

	public LocalDate getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(LocalDate checkOut) {
		this.checkOut = checkOut;
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(LocalDate checkIn) {
		this.checkIn = checkIn;
	}

	public String toString() {
		return this.checkIn + "////" + this.checkOut;

	}
}
