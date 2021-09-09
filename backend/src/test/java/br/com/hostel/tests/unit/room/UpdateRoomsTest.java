package br.com.hostel.tests.unit.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.form.RoomUpdateForm;
import br.com.hostel.exceptions.BaseException;
import br.com.hostel.initializer.RoomInitializer;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;
import br.com.hostel.service.RoomService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UpdateRoomsTest {

	@MockBean
	private RoomRepository roomRepository;
	
	@MockBean
	private DailyRateRepository dailyRepository;
	
	@MockBean
	private ReservationRepository reservationRepository;

	@MockBean
	private RoomUpdateForm form;

	@MockBean
	private UriComponentsBuilder uriBuilder;

	@Autowired
	private RoomService service;
	
	private static Room room = new Room();
	private static DailyRate dailyRate = new DailyRate();

	@BeforeAll
	public static void beforeAll() throws Exception {

		RoomInitializer.initialize(room, dailyRate);
	}

	@Test
	public void shouldUpdateRoomNumberAndDescription() throws Exception {

		Optional<Room> opRoom = Optional.of(room);
		
		opRoom.get().setNumber(13);
		opRoom.get().setDescription("Room updated");

		when(roomRepository.findById(room.getId())).thenReturn(opRoom);
		when(form.updateRoomForm(room.getId(), room, roomRepository)).thenReturn(room);
		when(dailyRepository.save(room.getDailyRate())).thenReturn(dailyRate);
		
		Room reqRoom = service.updateRoom(room.getId(), form, uriBuilder);
		
		assertEquals(opRoom.get().getNumber(), reqRoom.getNumber());
		assertEquals(opRoom.get().getDescription(), reqRoom.getDescription());
	}
	
	@Test
	public void shouldNotUpdateRoomWithNonexistenteID() throws Exception {
		
		Optional<Room> nonexistentRoom = Optional.empty();
		
		when(roomRepository.findById(room.getId())).thenReturn(nonexistentRoom);
		
		BaseException thrown = 
				assertThrows(BaseException.class, 
					() -> service.updateRoom(room.getId(), form, uriBuilder),
					"It was expected that updateRoom() thrown an exception, " +
					"due to trying to update a room with an nonexistent number");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}
}
