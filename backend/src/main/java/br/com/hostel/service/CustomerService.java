package br.com.hostel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.CustomerDto;
import br.com.hostel.controller.form.CustomerForm;
import br.com.hostel.model.Customer;
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
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There already exists a customer with that e-mail!");
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

	public ResponseEntity<CustomerDto> listOneCustomer(Long id) {
		
		Optional<Customer> customer = customerRepository.findById(id);
		
		return ResponseEntity.ok(new CustomerDto(customer.get()));
	}

	public ResponseEntity<?> deleteCustomer(Long id) {
		
		Optional<Customer> customer = customerRepository.findById(id);
		
		if (customer.isPresent()) {
			customerRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There isn't a customer with that ID");
	}
	
}
