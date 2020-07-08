package br.com.hostel.tests;
//package br.com.albergue.tests;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.time.LocalDate;
//import java.util.Date;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import br.com.albergue.controller.dto.CustomerDto;
//import br.com.albergue.domain.Address;
//import br.com.albergue.domain.Customer;
//import br.com.albergue.repository.AddressRepository;
//import br.com.albergue.repository.CustomerRepository;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:test.properties")
//public class Teste {
//
//	@Autowired
//	CustomerRepository customerRespository;
//
//	@Autowired
//	AddressRepository addressRepository;
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	ObjectMapper objectMapper;
//
//	@Value("${forum.jwt.expiration}") // @value serve para pegar a propriedade do application.properties
//	private String expiration;
//
//	@Value("${forum.jwt.secret}")
//	private String secret;
//
//	private URI uri;
//	private HttpHeaders headers = new HttpHeaders();
//	private String token;
//	private Address address = new Address();
//	private Customer customer = new Customer();
//
//	@Before
//	public void init() throws URISyntaxException {
//		uri = new URI("/api/customers");
//
//		// generating token to autentication
//		Date hoje = new Date();
//		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
//		token = Jwts.builder().setIssuer("API do Albergue") // quem fez a geração do token
//				.setSubject(Long.toString(1L)) // usuario a quem esse token pertence
//				.setIssuedAt(hoje) // data de geração
//				.setExpiration(dataExpiracao) // data de expiração
//				.signWith(SignatureAlgorithm.HS256, secret) // usar a senha do application.properties / algoritmo de
//															// criptografia
//				.compact();
//
//		// seting header to put on post and delete request parameters
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.set("Authorization", "Bearer " + token);
//
//		// setting address to put into the customer paramseters
//		address.setAddressName("rua x");
//		address.setCity("Amparo");
//		address.setCountry("Brasil");
//		address.setState("SP");
//		address.setZipCode("13900-000");
//
//		// setting customer
//		customer.setId(13L);
//		customer.setAddress(address);
//		customer.setBirthday(LocalDate.of(1900, 12, 12));
//		customer.setEmail("washington@orkut.com");
//		customer.setName("Washington");
//		customer.setLastName("Ferrolho");
//		customer.setTitle("MRS.");
//		customer.setPassword("1234567");
//	}
//
//	@Test
//	public void shouldAutenticateAndDeleteOneCustomerWithId2() throws Exception {
//		addressRepository.save(address);
//		customerRespository.save(customer);
//
//		mockMvc
//			.perform(delete("/api/customers/13")
//			.headers(headers))
//			.andExpect(status().isOk())
//			.andReturn();	
//	}
//	
//	@Test
//	public void shouldAutenticateAndCreateOneCustomerAndReturnStatusCreated() throws Exception {
//
//		MvcResult result = 
//				mockMvc
//					.perform(post(uri)
//					.headers(headers)
//					.content(objectMapper.writeValueAsString(customer)))
//					.andDo(print())
//					.andExpect(status().isCreated())
//					.andReturn();
//
//		String contentAsString = result.getResponse().getContentAsString();
//
//		CustomerDto customerObjResponse = objectMapper.readValue(contentAsString, CustomerDto.class);
//
//		assertEquals(customerObjResponse.getName(), "Washington");
//		assertEquals(customerObjResponse.getAddress().getCity(), "Amparo");
//	}
//
//}