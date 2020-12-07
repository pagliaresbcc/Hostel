package br.com.hostel.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.hostel.model.Address;
import br.com.hostel.model.Customer;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Role;

public class CustomerDto {

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

	public CustomerDto() {
	}

	public CustomerDto(Customer customer) {
		this.id = customer.getId();
		this.title = customer.getTitle();
		this.name = customer.getName();
		this.lastName = customer.getLastName();
		this.birthday = customer.getBirthday();
		this.address = customer.getAddress();
		this.email = customer.getEmail();
		this.password = customer.getPassword();
		this.role = customer.getRole();
		this.reservations = customer.getReservations();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public Address getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}
	
	public Role getRole() {
		return role;
	}
	
	public static List<CustomerDto> converter(List<Customer> customersList) {

		List<CustomerDto> customersDtoList = new ArrayList<>();
		
		customersList.forEach(customer -> customersDtoList.add(new CustomerDto(customer)));

		return customersDtoList;
	}
}