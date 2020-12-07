package br.com.hostel.controller.form;

import java.time.LocalDate;

import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Role;
import br.com.hostel.repository.GuestRepository;

public class GuestUpdateForm {
	
	private String title;
	private String name;
	private String lastname; 
	private LocalDate birthday;
	private Address address;
	private Role role;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public void setLastName(String lastname) {
		this.lastname = lastname;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public LocalDate getBirthday() {
		return birthday;
	}
	
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Guest updateGuestForm(Long id, Guest guest, GuestRepository guestRepository) {
		verifyIfParamIsNotNull(guest, guestRepository);
		return guest;
	}
	
	private void verifyIfParamIsNotNull(Guest guest, GuestRepository guestRepository) {
		
		if (title != null)
			guest.setTitle(title);
		
		if (name != null) 
			guest.setName(name);
		
		if (lastname != null) 
			guest.setLastName(lastname);
		
		if(birthday != null) 
			guest.setBirthday(birthday);
		
		if (address != null) 
			guest.setAddress(address);
		
		if(role != null)
			guest.setRole(role);
		
	}
	
}
