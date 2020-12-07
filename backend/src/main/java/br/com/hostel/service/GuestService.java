package br.com.hostel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.CustomerDto;
import br.com.hostel.controller.dto.ReservationDto;
import br.com.hostel.controller.form.CustomerForm;
import br.com.hostel.controller.form.CustomerUpdateForm;
import br.com.hostel.model.Customer;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Role;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AddressRepository addressRepository;

	public ResponseEntity<?> registerCustomer(CustomerForm form, UriComponentsBuilder uriBuilder) {
		Optional<Customer> customerEmail = customerRepository.findByEmail(form.getEmail());
		
		if(!customerEmail.isPresent()) {
			
			Customer customer = form.returnCustomer(addressRepository);
			customerRepository.save(customer);

			URI uri = uriBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri();
			return ResponseEntity.created(uri).body(new CustomerDto(customer));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There already exists a customer with e-mail = " + form.getEmail());
		}
	}

	public ResponseEntity<List<CustomerDto>> listAllCustomers(String name, Pageable pagination) {
		
		List<CustomerDto> response = new ArrayList<>();

		if (name == null)
			response = CustomerDto.converter(customerRepository.findAll());
		else 
			response = CustomerDto.converter(customerRepository.findByName(name));

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<?> listOneCustomer(Long id) {
		
		Optional<Customer> customer = customerRepository.findById(id);
		
		if (customer.isPresent()) return ResponseEntity.ok(new CustomerDto(customer.get()));
		
		else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a customer with id = " + id);
	}
	
	public ResponseEntity<?> listCustomerReservations(Long customer_ID) {

		Optional<Customer> customer = customerRepository.findById(customer_ID);
		
		if (customer.isPresent()) {
			List<Reservation> reservations = customer.get().getReservations().stream().collect(Collectors.toList());
			
			Collections.sort(reservations);
			
			List<ReservationDto> response = ReservationDto.converter(reservations);
	
			return ResponseEntity.ok(response);
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a customer with id = " + customer_ID);
	}

	public ResponseEntity<?> deleteCustomer(Long id) {
		
		Optional<Customer> customer = customerRepository.findById(id);
		
		if (customer.isPresent()) {
			customerRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a customer with id = " + id);
	}

	public ResponseEntity<?> updateCustomer(Long id, @Valid CustomerUpdateForm form) {
		Optional<Customer> customerOp = customerRepository.findById(id);

		if (customerOp.isPresent()) {
			
			Customer customer = form.updateCustomerForm(id, customerOp.get(), customerRepository);
			addressRepository.save(customer.getAddress());

			return ResponseEntity.ok(new CustomerDto(customer));
		} else 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn't a customer with id = " + id);
	}
	
}
