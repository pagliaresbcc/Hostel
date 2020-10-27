package br.com.hostel.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Reservation implements Comparable<Reservation>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private Long customer_ID;
	@Column(nullable=false)
	private int numberOfGuests;
	@Column(nullable=false)
	private LocalDate reservationDate;
	@Column(nullable=false)
	private LocalDate checkinDate;
	@Column(nullable=false)
	private LocalDate checkoutDate;
	
	@ManyToMany
	@Column(nullable=false)
	private Set<Room> rooms = new HashSet<>();
	
	@OneToOne(cascade=CascadeType.REMOVE)
	@JoinColumn(name = "payment_ID", nullable = false)
	private Payments payment;
	
	public Reservation(Long customer_ID, int numberOfGuests,LocalDate reservationDate, LocalDate checkinDate, LocalDate checkoutDate, Set<Room> rooms, Payments payment) {
		this.customer_ID = customer_ID;
		this.numberOfGuests = numberOfGuests;
		this.reservationDate = reservationDate;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.rooms = rooms;
		this.payment = payment;
	}

	public Reservation() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void addRoom(Room room) {
		this.rooms.add(room);
	}

	public Set<Room> getRooms() {
		return rooms;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public Payments getPayment() {
		return payment;
	}

	public void setPayment(Payments payment) {
		this.payment = payment;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	public Long getCustomer_ID() {
		return customer_ID;
	}

	public void setCustomer_ID(Long customer_ID) {
		this.customer_ID = customer_ID;
	}

	@Override
	public int compareTo(Reservation otherReservation) {
		return this.getId().compareTo(otherReservation.getId());
	}
	
}
