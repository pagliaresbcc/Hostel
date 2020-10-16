package br.com.hostel.tests.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.net.URI;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.CustomerDto;
import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.model.Address;
import br.com.hostel.model.Customer;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.CustomerRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class CustomerGetTests {

	@Autowired
	CustomerRepository customerRespository;

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	private URI uri;
	private HttpHeaders headers = new HttpHeaders();
	private Address address = new Address();
	private Customer customer = new Customer();
	private LoginForm login = new LoginForm();
	
	@BeforeEach
	public void init() throws JsonProcessingException, Exception {
		uri = new URI("/api/customers/");
		
		//setting login variables to autenticate
		login.setEmail("aluno@email.com");
		login.setPassword("123456");

		//posting on /auth to get token
		MvcResult resultAuth = mockMvc
				.perform(post("/auth")
				.content(objectMapper.writeValueAsString(login)).contentType("application/json"))
				.andReturn();	
			
		String contentAsString = resultAuth.getResponse().getContentAsString();

		LoginDto loginObjResponse = objectMapper.readValue(contentAsString, LoginDto.class);
		
		// seting header to put on post and delete request parameters
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + loginObjResponse.getToken());
		
		// setting address to put into the customer paramseters
		address.setAddressName("rua x");
		address.setCity("Amparo");
		address.setCountry("Brasil");
		address.setState("SP");
		address.setZipCode("13900-000");
		
		// setting customer
		customer.setAddress(address);
		customer.setBirthday(LocalDate.of(1900, 12, 12));
		customer.setEmail("washington1@orkut.com");
		customer.setName("Washington");
		customer.setLastName("Ferrolho");
		customer.setTitle("MRS.");
		customer.setPassword("1234567");
		
		addressRepository.save(address);
		customer = customerRespository.save(customer);
	}
	
	@Test
	public void shouldReturnAllCustomersWithoutParamAndStatusOk() throws Exception {
		
		Customer customer2 = customer;
		customer2.setName("Antonio");
		customer2.setLastName("Ferreira");
		customerRespository.save(customer2);
		
		MvcResult result = 
				mockMvc.perform(get(uri)
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		CustomerDto[] customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto[].class);
		
		/// Verify request succeed
		assertTrue(customerObjResponse.length >= 2);
	}
	
	@Test
	public void shouldReturnOneCustomerByParamAndStatusOk() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Washington")
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		CustomerDto[] customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto[].class);
		
		/// Verify request succeed
		assertEquals(1, customerObjResponse.length, 1);
		assertEquals(customer.getName(), customerObjResponse[0].getName());
		assertEquals(customer.getAddress().getCity(), customerObjResponse[0].getAddress().getCity());
	}
	
	@Test
	public void shouldReturnOneCustomerByIdAndStatusOk() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri + customer.getId().toString())
						.headers(headers))
						.andDo(print())
						.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		CustomerDto customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto.class);
		
		/// Verify request succeed
		assertEquals(customer.getName(), customerObjResponse.getName());
		assertEquals(customer.getAddress().getCity(), customerObjResponse.getAddress().getCity());

	}


	@Test
	public void shouldReturEmptyBodyByWrongParam() throws Exception {

		MvcResult result = 
				mockMvc.perform(get(uri)
						.param("name", "Washington222")
						.headers(headers))
						.andDo(print())
						.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		CustomerDto[] customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto[].class);
		
		assertEquals(0, customerObjResponse.length);
	}

}