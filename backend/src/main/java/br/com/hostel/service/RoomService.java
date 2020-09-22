package br.com.hostel.service;

import java.net.URI;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.RoomForm;
import br.com.hostel.controller.form.RoomUpdateForm;
import br.com.hostel.controller.helper.RoomFilter;
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

	public ResponseEntity<?> registerRoom(RoomForm form, UriComponentsBuilder uriBuilder) {
		
		Room room = form.returnRoom(dailyRateRepository);

		Optional<Room> roomOp = roomRepository.findByNumber(room.getNumber());

		if (roomOp.isPresent()) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There already exists a room with this number.");
		else if (roomOp.get().getNumber() == 0) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Room's number is null");
	    else {
			roomRepository.save(room);

			URI uri = uriBuilder.path("/rooms/{id}").buildAndExpand(room.getId()).toUri();
			return ResponseEntity.created(uri).body(new RoomDto(room));
		}
	}

	public ResponseEntity<List<RoomDto>> listAllRooms(RoomFilter roomFilter, Pageable pagination) {

		List<RoomDto> response = new ArrayList<>();
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

			unavailableRooms.forEach(room -> availableRooms.remove(room));
		}
		response = RoomDto.convert(availableRooms);

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<RoomDto> listOneRoom(Long id) {
		
		Optional<Room> room = roomRepository.findById(id);

		return ResponseEntity.ok(new RoomDto(room.get()));
	}

	public ResponseEntity<RoomDto> updateRoom(@PathVariable Long id, @RequestBody @Valid RoomUpdateForm form,
			UriComponentsBuilder uriBuilder) {

		Optional<Room> roomOp = roomRepository.findById(id);

		Room room = form.updateRoomForm(id, roomOp.get(), roomRepository);

		dailyRateRepository.save(room.getDailyRate());

		roomRepository.save(room);

		return ResponseEntity.ok(new RoomDto(room));
	}

	public ResponseEntity<?> deleteRoom(Long id) {
		
		Optional<Room> room = roomRepository.findById(id);

		if (room.isPresent()) {
			roomRepository.deleteById(id);

			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There isn't a room with that ID");
	}

	private void verifyValidRooms(RoomFilter roomFilter, List<Room> unavailableRooms, List<Room> availableRooms) {

		if (roomFilter.getMinDailyRate() != null) {
			availableRooms.forEach(room -> {
				if (room.getDailyRate().getPrice() < roomFilter.getMinDailyRate()) {
					unavailableRooms.add(room);
				}
			});
		}

		unavailableRooms.forEach(room -> availableRooms.remove(room));

		if (roomFilter.getMaxDailyRate() != null) {
			availableRooms.forEach(room -> {
				if (room.getDailyRate().getPrice() > roomFilter.getMaxDailyRate()) {
					unavailableRooms.add(room);
				}
			});
		}

		unavailableRooms.forEach(room -> availableRooms.remove(room));

		if (roomFilter.getNumberOfGuests() != null) {
			availableRooms.forEach(room -> {
				if (room.getMaxNumberOfGuests() < roomFilter.getNumberOfGuests()) {
					unavailableRooms.add(room);
				}
			});
		}

		unavailableRooms.forEach(room -> availableRooms.remove(room));
	}

	private void verifyValidRoomsWithinAPeriod(List<Room> unavailableRooms, LocalDate checkinDate, LocalDate checkoutDate,
			Reservation reservation) {
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
