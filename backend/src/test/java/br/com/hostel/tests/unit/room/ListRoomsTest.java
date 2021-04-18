package br.com.hostel.tests.unit.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hostel.controller.dto.LoginDto;
import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.LoginForm;
import br.com.hostel.controller.form.ReservationForm;
import br.com.hostel.controller.helper.RoomFilter;
import br.com.hostel.exceptions.BaseException;
import br.com.hostel.model.CheckPayment;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;
import br.com.hostel.service.RoomService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ListRoomsTest {

	@MockBean
	RoomRepository roomRepository;

	@MockBean
	DailyRateRepository dailyRepository;

	@MockBean
	ReservationRepository reservationRepository;

	@Autowired
	RoomService service;

	private static Room firstRoom = new Room();
	private static Room secondRoom = new Room();
	private static DailyRate dailyRate = new DailyRate();
	private static List<Room> roomsList = new ArrayList<>();
	
	private static RoomFilter filter = new RoomFilter();

	@BeforeAll
	public static void beforeAll() {
		// setting daily rate to put into the room parameters
		dailyRate.setPrice(400);

		// setting first room
		firstRoom.setDailyRate(dailyRate);
		firstRoom.setDescription("Room first test");
		firstRoom.setDimension(230.0);
		firstRoom.setMaxNumberOfGuests(4);
		firstRoom.setNumber(666);

		// setting second room
		secondRoom.setDailyRate(dailyRate);
		secondRoom.setDescription("Room second test");
		secondRoom.setDimension(460.0);
		secondRoom.setMaxNumberOfGuests(8);
		secondRoom.setNumber(777);

		roomsList.add(firstRoom);
		roomsList.add(secondRoom);
	}

	@Test
	public void shouldReturnAllRoomsWithoutParamAndStatusOk() throws Exception {

		when(roomRepository.findAll()).thenReturn(roomsList);
		
		List<Room> listAllRooms = service.listAllRooms(filter, null);
		
		assertEquals(roomsList.size(), listAllRooms.size());
	}
	
	@Test
	public void shouldReturnOneRoomAndStatusOkByParam() throws Exception {

		List<Room> rooms = new ArrayList<>();
		rooms.add(secondRoom);
		
		when(roomRepository.findAll()).thenReturn(rooms);
		
		List<Room> justOneRoomList = service.listAllRooms(filter, null);
		
		assertEquals(1, justOneRoomList.size());
		assertEquals(secondRoom.getNumber(), justOneRoomList.get(0).getNumber());
	}
	
	@Test
	public void shouldReturnEmptyListByUsingFilterByNumberOfGuests() throws Exception {
		
		List<Room> emptyList = new ArrayList<>();
		
		when(roomRepository.findAll()).thenReturn(roomsList);
		
		RoomFilter newFilter = new RoomFilter();
		
		newFilter.setNumberOfGuests(10);
		
		List<Room> reqEmptyList = service.listAllRooms(newFilter, null);
		
		assertEquals(emptyList.size(), reqEmptyList.size());
	}
	
	@Test
	public void shouldReturnOneRoomAndStatusOkByID() throws Exception {

		Optional<Room> opRoom = Optional.of(firstRoom);
		
		when(roomRepository.findById(firstRoom.getId())).thenReturn(opRoom);

		Room reqRoom = service.listOneRoom(opRoom.get().getId());

		assertEquals(opRoom.get().getNumber(), reqRoom.getNumber());
		assertEquals(opRoom.get().getMaxNumberOfGuests(), reqRoom.getMaxNumberOfGuests());
	}
	
	@Test
	public void shouldThrowExceptionByFindARoomWithNonexistentID() throws Exception {

		Optional<Room> opRoom = Optional.empty();
		
		when(roomRepository.findById(firstRoom.getId())).thenReturn(opRoom);

		BaseException thrown = 
				assertThrows(BaseException.class, 
					() -> service.listOneRoom(firstRoom.getId()),
					"Expected listOneRoom() to throw, but it didn't");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}
}