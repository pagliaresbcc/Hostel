package br.com.hostel.tests.unit.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hostel.controller.helper.RoomFilter;
import br.com.hostel.exceptions.room.RoomException;
import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;
import br.com.hostel.service.RoomService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ListRoomsTest {

	@MockBean
	private RoomRepository roomRepository;

	@MockBean
	private DailyRateRepository dailyRepository;

	@MockBean
	private ReservationRepository reservationRepository;

	@Autowired
	private RoomService service;

	private static Reservation firstReservation = new Reservation();
	private static Reservation secondReservation = new Reservation();
	private static Reservation thirdReservation = new Reservation();
	private static List<Reservation> reservationsList = new ArrayList<>();
	private static Set<Room> rooms = new HashSet<>();
	private static Room firstRoom = new Room();
	private static Room secondRoom = new Room();
	private static Room thirdRoom = new Room();
	private static Room fourthRoom = new Room();
	private static DailyRate firstDailyRate = new DailyRate();
	private static DailyRate secondDailyRate = new DailyRate();
	private static DailyRate thirdDailyRate = new DailyRate();
	private static List<Room> roomsList = new ArrayList<>();

	private static RoomFilter filter = new RoomFilter();

	@BeforeAll
	public static void beforeAll() {		
		// setting daily rate to put into the room parameters
		firstDailyRate.setPrice(5001);
		secondDailyRate.setPrice(400);
		thirdDailyRate.setPrice(0.0);

		// setting first room
		firstRoom.setDailyRate(firstDailyRate);
		firstRoom.setDescription("Room first test");
		firstRoom.setDimension(230.0);
		firstRoom.setMaxNumberOfGuests(4);
		firstRoom.setNumber(666);

		// setting second room
		secondRoom.setDailyRate(secondDailyRate);
		secondRoom.setDescription("Room second test");
		secondRoom.setDimension(460.0);
		secondRoom.setMaxNumberOfGuests(8);
		secondRoom.setNumber(777);

		// setting third room
		thirdRoom.setDailyRate(thirdDailyRate);
		thirdRoom.setDescription("Room third test");
		thirdRoom.setDimension(9000.0);
		thirdRoom.setMaxNumberOfGuests(28);
		thirdRoom.setNumber(999);

		// setting fourth  room
		fourthRoom.setDailyRate(secondDailyRate);
		fourthRoom.setDescription("Room third test");
		fourthRoom.setDimension(200.0);
		fourthRoom.setMaxNumberOfGuests(1);
		fourthRoom.setNumber(111);

		roomsList.add(firstRoom);
		roomsList.add(secondRoom);
		roomsList.add(thirdRoom);
		roomsList.add(fourthRoom);

		filter.setCheckinDate("2020-10-09");
		filter.setCheckoutDate("2020-10-11");
		filter.setMaxDailyRate(5000.0);
		filter.setMinDailyRate(100.0);
		filter.setNumberOfGuests(5);

		rooms.add(firstRoom);
		rooms.add(secondRoom);
		rooms.add(thirdRoom);
		
		// setting first reservation
		firstReservation.setCheckinDate(LocalDate.of(2020, 10, 07));
		firstReservation.setCheckoutDate(LocalDate.of(2020, 10, 10));
		firstReservation.setNumberOfGuests(4);
		firstReservation.setRooms(rooms);
		// setting second reservation
		secondReservation.setCheckinDate(LocalDate.of(2020, 10, 10));
		secondReservation.setCheckoutDate(LocalDate.of(2020, 10, 11));
		secondReservation.setNumberOfGuests(2);
		firstReservation.setRooms(rooms);
		// setting third reservation
		thirdReservation.setCheckinDate(LocalDate.of(2025, 11, 11));
		thirdReservation.setCheckoutDate(LocalDate.of(2025, 11, 15));
		thirdReservation.setNumberOfGuests(8);
		firstReservation.setRooms(rooms);

		reservationsList.add(firstReservation);
		reservationsList.add(secondReservation);
		reservationsList.add(thirdReservation);
	}

	@Test
	public void shouldReturnAllRoomsWithoutParamAndStatusOk() throws Exception {

		when(roomRepository.findAll()).thenReturn(roomsList);
		when(reservationRepository.findAll()).thenReturn(reservationsList);
		
		List<Room> listAllRooms = service.listAllRooms(filter, null);

		assertEquals(roomsList.size(), listAllRooms.size());
	}

	@Test
	public void shouldReturnOneRoomAndStatusOkByParam() throws Exception {

		List<Room> rooms = new ArrayList<>();
		rooms.add(firstRoom);
		rooms.add(secondRoom);
		rooms.add(thirdRoom);

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

		RoomException thrown = assertThrows(RoomException.class, 
				() -> service.listOneRoom(firstRoom.getId()),
				"It was expected that listOneRoom() thrown an exception, " + 
				"due to a nonexistent ID");

		assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
	}
}
