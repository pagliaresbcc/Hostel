package br.com.hostel.tests.unit.room;

import br.com.hostel.exceptions.room.RoomException;
import br.com.hostel.model.Room;
import br.com.hostel.repository.RoomRepository;
import br.com.hostel.service.RoomService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DeleteRoomsTest {
	
	@MockBean
	private RoomRepository roomRepository;

	@MockBean
	private Room room;

	@Autowired
	private RoomService service;

	@Test
	public void shouldReturnNotFoundStatusAfterDeletingARoomWithNonExistentID() {

		Optional<Room> nonexistentRoom = Optional.empty();

		when(roomRepository.findById(any())).thenReturn(nonexistentRoom);

		RoomException thrown = 
				assertThrows(RoomException.class, 
					() -> service.deleteRoom(room.getId()),
					"It was expected that deleteRoom() thrown an exception, " +
					"due to a nonexistent ID");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}
	
	@Test
	public void shouldReturnNotFoundStatusAfterTryingToDeleteARoomWithExistentID() {

		Optional<Room> opRoom = Optional.of(room);

		when(roomRepository.findById(any())).thenReturn(opRoom).thenReturn(Optional.empty());

		service.deleteRoom(room.getId());
		
		RoomException thrown = 
				assertThrows(RoomException.class, 
					() -> service.deleteRoom(room.getId()),
					"It was expected that deleteRoom() thrown an exception, " +
					"due to trying to find the room that have been deleted");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}
}
