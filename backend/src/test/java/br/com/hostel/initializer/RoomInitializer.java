package br.com.hostel.initializer;

import java.util.Calendar;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RoomInitializer {

	public static void initialize(Room room, DailyRate dailyRate) throws Exception {
		
		dailyRate.setPrice(400);
		
		Calendar calendar = Calendar.getInstance();
		
		room.setNumber(calendar.get(Calendar.SECOND) + 100);
		room.setDescription("Room test");
		room.setDimension(230.0);
		room.setMaxNumberOfGuests(4);
		room.setDailyRate(dailyRate);
	}
}
