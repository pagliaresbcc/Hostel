package br.com.hostel.initializer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Calendar;

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
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RoomInitializer {

	public static void initialize(HttpHeaders headers, Room room, DailyRate dailyRate, 
			MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {

		LoginForm login = new LoginForm();

		//setting login variables to autenticate
		login.setEmail("maria@email.com");
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
		
		dailyRate.setPrice(400);
		
		Calendar calendar = Calendar.getInstance();
		
		room.setNumber(calendar.get(Calendar.SECOND) + 100);
		room.setDescription("room test");
		room.setDimension(230.0);
		room.setMaxNumberOfGuests(4);
		room.setDailyRate(dailyRate);
	}
}
