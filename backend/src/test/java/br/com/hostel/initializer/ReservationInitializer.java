package br.com.hostel.initializer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.model.CheckPayment;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ReservationInitializer {

	public static void initialize(HttpHeaders headers, ReservationForm reservationForm, CheckPayment checkPayment, 
			List<Long> rooms_ID, MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {

		LoginForm login = new LoginForm();

		// setting login variables to autenticate
		login.setEmail("maria@email.com");
		login.setPassword("123456");

		// posting on /auth to get token
		MvcResult resultAuth = mockMvc
				.perform(post("/auth").content(objectMapper.writeValueAsString(login)).contentType("application/json"))
				.andReturn();

		String contentAsString = resultAuth.getResponse().getContentAsString();

		LoginDto loginObjResponse = objectMapper.readValue(contentAsString, LoginDto.class);

		// seting header to put on post and delete request parameters
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + loginObjResponse.getToken());

		// setting reservation object
		reservationForm.setCheckinDate(LocalDate.of(2025, 04, 01));
		reservationForm.setCheckoutDate(LocalDate.of(2025, 04, 04));
		reservationForm.setNumberOfGuests(2);
		reservationForm.setGuest_ID(1L);
		reservationForm.setGuestName("Maria");
		

		checkPayment.setAmount(3000);
		checkPayment.setDate(LocalDateTime.of(LocalDate.of(2025, 01, 25), LocalTime.of(21, 31)));
		checkPayment.setBankId("01");
		checkPayment.setBankName("Banco do Brasil");
		checkPayment.setBranchNumber("1234-5");

		reservationForm.setPayment(checkPayment);

		rooms_ID.add(2L);
		reservationForm.setRooms_ID(rooms_ID);
	}
}
