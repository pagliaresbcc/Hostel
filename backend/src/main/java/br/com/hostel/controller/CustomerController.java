package br.com.hostel.controller;

import java.net.URISyntaxException;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.CustomerDto;
import br.com.hostel.controller.form.CustomerForm;
import br.com.hostel.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	@PostMapping
	public ResponseEntity<?> registerCustomer(@RequestBody @Valid CustomerForm form,
			UriComponentsBuilder uriBuilder) {

		return this.customerService.registerCustomer(form, uriBuilder);
	}

	@GetMapping
	public ResponseEntity<List<CustomerDto>> listAllCustomers(@RequestParam(required = false) String name,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable pagination)
			throws URISyntaxException {

		return this.customerService.listAllCustomers(name, pagination);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> listOneCustomer(@PathVariable Long id) {

		return this.customerService.listOneCustomer(id);
	}
	
	@GetMapping("/{id}/reservations")
	public ResponseEntity<?> listCustomerReservations(@PathVariable Long id) {

		return this.customerService.listCustomerReservations(id);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {

		return this.customerService.deleteCustomer(id);
	}
}
