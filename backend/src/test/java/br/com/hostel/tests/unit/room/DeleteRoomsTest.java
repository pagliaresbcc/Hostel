package br.com.hostel.tests.unit.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hostel.exceptions.BaseException;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;
import br.com.hostel.service.RoomService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DeleteRoomsTest {
	
	@MockBean
	RoomRepository roomRepository;
	
	@MockBean
	DailyRateRepository dailyRepository;
	
	@MockBean
	ReservationRepository reservationRepository;

	@MockBean
	Room room;

	@Autowired
	RoomService service;

	@Test
	public void shouldReturnTrueWhenDeletingARoomWithExistentID() throws Exception {

		Optional<Room> opRoom = Optional.of(room);

		when(roomRepository.findById(any())).thenReturn(opRoom);

		service.deleteRoom(room.getId());
	}

	@Test
	public void shouldReturnFalseWhenDeletingARoomWithNonExistentID() throws Exception {

		Optional<Room> nonexistentRoom = Optional.empty();

		when(roomRepository.findById(any())).thenReturn(nonexistentRoom);

		BaseException thrown = 
				assertThrows(BaseException.class, 
					() -> service.deleteRoom(room.getId()),
					"It was expected that deleteRoom() thrown an exception, " +
					"due to a nonexistent ID");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}
}
