package br.com.hostel.controller.form;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.hostel.model.Address;
import br.com.hostel.model.Customer;
import br.com.hostel.model.Role;
import br.com.hostel.repository.AddressRepository;

public class CustomerForm {
	
	@NotNull 
	private String title;
	@NotNull 
	private String name;
	@NotNull 
	private String lastname;
	@NotNull 
	private LocalDate birthday;
	@NotNull 
	private Address address;
	@NotNull 
	private String email;
	@NotNull 
	private String password;
	@NotNull
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
	
	public String getLastName() {
		return lastname;
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Customer returnCustomer(AddressRepository addressRepository) {
		addressRepository.save(address);
		
		return new Customer(title, name, lastname, birthday, address, email, 
				new BCryptPasswordEncoder().encode(password), role);
	}
}
