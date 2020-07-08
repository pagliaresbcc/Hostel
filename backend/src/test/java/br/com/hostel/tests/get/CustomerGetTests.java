package br.com.hostel.tests.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.CustomerDto;
import br.com.hostel.model.Address;
import br.com.hostel.model.Customer;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.CustomerRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CustomerGetTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	AddressRepository addressRepository;
	
	@Autowired
	ObjectMapper objectMapper;

	private URI uri;
	private Address address = new Address();
	private Customer customer = new Customer();
	private List<Customer> customersList = new ArrayList<>();
	
	@BeforeEach
	public void init() throws URISyntaxException {

		uri = new URI("/api/customers");

		// setting address to put into the customer paramseters
		address.setAddressName("rua x");
		address.setCity("Amparo");
		address.setCountry("Brasil");
		address.setState("SP");
		address.setZipCode("13900-000");
		
		// setting customer
		customer.setId(1L);
		customer.setLastName("aaaaaaaaaa");
		customer.setAddress(address);
		customer.setBirthday(LocalDate.of(1900, 12, 12));
		customer.setEmail("washington@orkut.com");
		customer.setName("Washington");
		customer.setTitle("MRS.");
		customer.setPassword("1234567");
		
		customersList.add(customer);
	}

	@Test
	public void shouldReturnOneCustomerWithoutParamAndStatusOk() throws Exception {
		Customer customer2 = new Customer();
		customer2.setName("Antonio");
		customersList.add(customer2);
		
		when(customerRepository.findAll()).thenReturn(customersList);

		MvcResult result = 
				mockMvc.perform(get(uri))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		CustomerDto[] customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto[].class);
		
		/// Verify request succeed
		assertEquals(customerObjResponse.length, 2);
		assertEquals(customerObjResponse[0].getName(), "Washington");
		assertEquals(customerObjResponse[0].getAddress().getCity(), "Amparo");

	}

	@Test
	public void shouldReturnOneCustomerByParamAndStatusOk() throws Exception {

		when(customerRepository.findByName(eq(customer.getName()))).thenReturn(customersList);
		
		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Washington"))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		CustomerDto[] customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto[].class);
		
		/// Verify request succeed
		assertEquals(customerObjResponse[0].getName(), "Washington");
		assertEquals(customerObjResponse[0].getAddress().getCity(), "Amparo");
	}

	@Test
	public void shouldReturnOneCustomerByIdAndStatusOk() throws Exception {

		Optional<Customer> opcust = Optional.of(customer);
		when(customerRepository.findById(eq(customer.getId()))).thenReturn(opcust);

		MvcResult result = 
				mockMvc.perform(get(uri+"/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		CustomerDto customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto.class);
		
		/// Verify request succeed
		assertEquals(customerObjResponse.getName(), "Washington");
		assertEquals(customerObjResponse.getAddress().getCity(), "Amparo");

	}

	@Test
	public void shouldNotReturnAnyCustomerByWrongParamAndStatusNotFound() throws Exception {

		when(customerRepository.findByName(eq(customer.getName()))).thenReturn(customersList);
		mockMvc.perform(get(uri)
				.param("name", "Washington222"))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andReturn();
	}
}