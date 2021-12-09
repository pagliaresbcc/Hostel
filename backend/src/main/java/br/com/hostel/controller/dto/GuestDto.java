package br.com.hostel.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.helper.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestDto {

	private Long id;
	private String title;
	private String name;
	private String lastName;
	private LocalDate birthday;
	private Address address;
	private String email;
	private String password;
	private Role role;
	private Set<Reservation> reservations;

	public GuestDto(Guest guest) {
		this.id = guest.getId();
		this.title = guest.getTitle();
		this.name = guest.getName();
		this.lastName = guest.getLastName();
		this.birthday = guest.getBirthday();
		this.address = guest.getAddress();
		this.email = guest.getEmail();
		this.password = guest.getPassword();
		this.role = guest.getRole();
		this.reservations = guest.getReservations();
	}

	public static List<GuestDto> converter(List<Guest> guestsList) {

		List<GuestDto> guestsDtoList = new ArrayList<>();
		
		guestsList.forEach(guest -> guestsDtoList.add(new GuestDto(guest)));

		return guestsDtoList;
	}
}