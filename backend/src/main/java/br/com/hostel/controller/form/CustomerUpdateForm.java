package br.com.hostel.controller.form;

import java.time.LocalDate;

import br.com.hostel.model.Address;
import br.com.hostel.model.Customer;
import br.com.hostel.repository.CustomerRepository;

public class CustomerUpdateForm {
	
	private String title;
	private String name;
	private String lastname; 
	private LocalDate birthday;
	private Address address; 
	private String email;
	private String password;
	
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
	
	public Customer updateCustomerForm(Long id, Customer customer, CustomerRepository customerRepository) {
		verifyIfParamIsNotNull(customer, customerRepository);
		return customer;
	}
	
	private void verifyIfParamIsNotNull(Customer customer, CustomerRepository customerRepository) {
		
		if (title != null)
			customer.setTitle(title);
		
		if (name != null) 
			customer.setName(name);
		
		if (lastname != null) 
			customer.setLastName(lastname);
		
		
		if(birthday != null) 
			customer.setBirthday(birthday);
		
		
		if (address != null) 
			customer.setAddress(address);
		
		
		if (email != null) 
			customer.setEmail(email);
		
		if (password != null) 
			customer.setPassword(password);
		
	}
	
}
