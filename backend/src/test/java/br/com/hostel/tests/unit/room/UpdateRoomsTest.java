package br.com.hostel.tests.unit.room;

import br.com.hostel.controller.form.RoomUpdateForm;
import br.com.hostel.exceptions.room.RoomException;
import br.com.hostel.initializer.RoomInitializer;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.RoomRepository;
import br.com.hostel.service.RoomService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UpdateRoomsTest {

	@MockBean
	private RoomRepository roomRepository;
	
	@MockBean
	private DailyRateRepository dailyRepository;

	@MockBean
	private RoomUpdateForm form;

	@Autowired
	private RoomService service;
	
	private static Room room = new Room();
	private static DailyRate dailyRate = new DailyRate();

	@BeforeAll
	public static void beforeAll() throws Exception {

		RoomInitializer.initialize(room, dailyRate);
	}

	@Test
	public void shouldUpdateRoomNumberAndDescription() {

		Optional<Room> opRoom = Optional.of(room);
		
		opRoom.get().setNumber(13);
		opRoom.get().setDescription("Room updated");

		when(roomRepository.findById(room.getId())).thenReturn(opRoom);
		when(form.updateRoomForm(room, roomRepository)).thenReturn(room);
		when(dailyRepository.save(room.getDailyRate())).thenReturn(dailyRate);
		
		Room reqRoom = service.updateRoom(room.getId(), form);
		
		assertEquals(opRoom.get().getNumber(), reqRoom.getNumber());
		assertEquals(opRoom.get().getDescription(), reqRoom.getDescription());
	}
	
	@Test
	public void shouldNotUpdateRoomWithNonexistentID() {
		
		Optional<Room> nonexistentRoom = Optional.empty();
		
		when(roomRepository.findById(room.getId())).thenReturn(nonexistentRoom);
		
		RoomException thrown = 
				assertThrows(RoomException.class, 
					() -> service.updateRoom(room.getId(), form),
					"It was expected that updateRoom() thrown an exception, " +
					"due to trying to update a room with an nonexistent number");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}
}
