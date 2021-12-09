package br.com.hostel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.form.RoomForm;
import br.com.hostel.controller.form.RoomUpdateForm;
import br.com.hostel.controller.helper.RoomFilter;
import br.com.hostel.exceptions.room.RoomException;
import br.com.hostel.model.Reservation;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.ReservationRepository;
import br.com.hostel.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private DailyRateRepository dailyRateRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	public Room registerRoom(RoomForm form) throws RoomException {

		Room room = form.returnRoom(dailyRateRepository);
		Optional<Room> roomOp = roomRepository.findByNumber(room.getNumber());

		if (roomOp.isPresent()) {
			throw new 
			RoomException("There is already a room with number = " + room.getNumber(),
					HttpStatus.BAD_REQUEST);
		}

		return roomRepository.save(room);
	}

	public List<Room> listAllRooms(RoomFilter roomFilter) {

		List<Room> unavailableRooms = new ArrayList<>();
		List<Room> availableRooms = roomRepository.findAll();

		verifyValidRooms(roomFilter, unavailableRooms, availableRooms);

		if (roomFilter.getCheckinDate() != null && roomFilter.getCheckoutDate() != null) {
			LocalDate checkinDate = LocalDate.parse(roomFilter.getCheckinDate());
			LocalDate checkoutDate = LocalDate.parse(roomFilter.getCheckoutDate());

			List<Reservation> reservationsList = reservationRepository.findAll();

			reservationsList.forEach(reservation -> {

				verifyValidRoomsWithinAPeriod(unavailableRooms, checkinDate, checkoutDate, reservation);

			});

			unavailableRooms.forEach(availableRooms::remove);
		}

		return availableRooms;
	}

	public Room listOneRoom(Long id) throws RoomException {

		Optional<Room> room = roomRepository.findById(id);

		if (room.isEmpty())
			throw new RoomException("Room ID haven't found", HttpStatus.NOT_FOUND);

		return room.get();

	}

	public Room updateRoom(@PathVariable Long id, @RequestBody @Valid RoomUpdateForm form) throws RoomException {

		Optional<Room> roomOp = roomRepository.findById(id);

		if (roomOp.isEmpty())
			throw new RoomException("Room ID haven't found", HttpStatus.NOT_FOUND);

		Room room = form.updateRoomForm(roomOp.get(), roomRepository);

		dailyRateRepository.save(room.getDailyRate());

		roomRepository.save(room);
		
		return room;
	}

	public void deleteRoom(Long id) throws RoomException {

		Optional<Room> room = roomRepository.findById(id);

		if (room.isEmpty())
			throw new RoomException("Room ID haven't found", HttpStatus.NOT_FOUND);

		roomRepository.deleteById(id);
	}

	private void verifyValidRooms(RoomFilter roomFilter, List<Room> unavailableRooms, List<Room> availableRooms) {

		if (roomFilter.getMinDailyRate() != null) {
			availableRooms.forEach(room -> {
				if (room.getDailyRate().getPrice() < roomFilter.getMinDailyRate()) {
					unavailableRooms.add(room);
				}
			});
		}

		unavailableRooms.forEach(availableRooms::remove);

		if (roomFilter.getMaxDailyRate() != null) {
			availableRooms.forEach(room -> {
				if (room.getDailyRate().getPrice() > roomFilter.getMaxDailyRate()) {
					unavailableRooms.add(room);
				}
			});
		}

		unavailableRooms.forEach(availableRooms::remove);

		if (roomFilter.getNumberOfGuests() != null) {
			availableRooms.forEach(room -> {
				if (room.getMaxNumberOfGuests() < roomFilter.getNumberOfGuests()) {
					unavailableRooms.add(room);
				}
			});
		}

		unavailableRooms.forEach(availableRooms::remove);
	}

	private void verifyValidRoomsWithinAPeriod(List<Room> unavailableRooms, LocalDate checkinDate,
			LocalDate checkoutDate, Reservation reservation) {
		long numOfDays = ChronoUnit.DAYS.between(reservation.getCheckinDate(), reservation.getCheckoutDate());

		List<LocalDate> dates = Stream.iterate(reservation.getCheckinDate(), date -> date.plusDays(1)).limit(numOfDays)
				.collect(Collectors.toList());

		if ((dates.contains(checkinDate) || dates.contains(checkoutDate))
				|| (checkinDate.isBefore(reservation.getCheckinDate())
						&& checkoutDate.isAfter(reservation.getCheckoutDate())
						|| checkoutDate.isEqual(reservation.getCheckoutDate()))) {

			reservation.getRooms().forEach(room -> {
				if (!unavailableRooms.contains(room)) {
					unavailableRooms.add(room);
				}
			});
		}
	}
}
