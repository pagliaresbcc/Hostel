package br.com.hostel.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Hostel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;
	@OneToOne @JoinColumn(name = "address_ID", nullable = false)
	private Address address = new Address();
	@Column(nullable = false)
	private String phone;

	@OneToMany
	private Set<Reservation> reservations;
	@OneToMany
	private Set<Guest> guests;
	@OneToMany
	private Set<Room> rooms;

	public Hostel() { 
		reservations = new HashSet<>();
		guests = new HashSet<>();
		rooms = new HashSet<>();
	}

	public Hostel(Long id, String name, String phone){
		this.id = id;
		this.name = name;
		this.phone = phone;
	}
	
	public Long getId(){
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Set<Guest> getGuests() {
		return guests;
	}

	public void setGuests(Set<Guest> guests) {
		this.guests = guests;
	}

	public Set<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}
}
