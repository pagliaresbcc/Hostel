package br.com.hostel.tests.unit.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import br.com.hostel.controller.form.RoomForm;
import br.com.hostel.exceptions.BaseException;
import br.com.hostel.initializer.RoomInitializer;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;
import br.com.hostel.service.RoomService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RoomService.class)
public class CreateRoomsTest {

	@MockBean
	private RoomRepository roomRepository;
	
	@MockBean
	private DailyRateRepository dailyRepository;
	
	@MockBean
	private ReservationRepository reservationRepository;

	@MockBean
	private RoomForm form;

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
	public void shouldCreateOneRoomSuccessfully() throws BaseException {

		Optional<Room> nonexistentRoom = Optional.empty();
		
		when(form.returnRoom(any())).thenReturn(room);
		when(roomRepository.findByNumber(any())).thenReturn(nonexistentRoom);
		when(roomRepository.save(any())).thenReturn(room);

		Room reqRoom = service.registerRoom(form, uriBuilder);

		assertEquals(room.getDescription(), reqRoom.getDescription());
		assertEquals(room.getDimension(), reqRoom.getDimension());

	}
	
	@Test
	public void shouldReturnNullWithExistentRoomNumber() throws BaseException {
		
		Optional<Room> opRoom = Optional.of(room);

		when(form.returnRoom(any())).thenReturn(room);
		when(roomRepository.findByNumber(any())).thenReturn(opRoom);
		
		BaseException thrown = 
				assertThrows(BaseException.class, 
					() -> service.registerRoom(form, uriBuilder),
					"It was expected that registerRoom() thrown an exception, " +
					"due to trying to create a room with an existing number");

		assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
	}
}
