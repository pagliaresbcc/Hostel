package br.com.hostel.tests.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class CustomerPostAndDeleteTests {

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
		customer.setEmail("washington@orkut.com");
		customer.setName("Washington");
		customer.setLastName("Ferrolho");
		customer.setTitle("MRS.");
		customer.setPassword("1234567");
		
	}
//	
//	@Test
//	public void testUpdateCustomerAttribute() {
//		CustomerUpdateForm customerUpdate = new CustomerUpdateForm();
//		
//		customerUpdate.setName("Vitor");
//		customerUpdate.setLastName("Muniz");
//	}
//	
	@Test
	public void shouldAutenticateAndDeleteOneCustomerWithId2() throws Exception {
		
		addressRepository.save(address);
		customerRespository.save(customer);

		mockMvc
			.perform(delete(uri + "2")
			.headers(headers))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();	
	}
	
	@Test
	public void shouldAutenticateAndCreateOneCustomerAndReturnStatusCreated() throws Exception {

		MvcResult result = 
				mockMvc
					.perform(post(uri)
					.headers(headers)
					.content(objectMapper.writeValueAsString(customer)))
					.andDo(print())
					.andExpect(status().isCreated())
					.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		CustomerDto customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto.class);

		assertEquals(customerObjResponse.getName(), "Washington");
		assertEquals(customerObjResponse.getAddress().getCity(), "Amparo");
	}

}