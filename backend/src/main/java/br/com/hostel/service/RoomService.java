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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.RoomForm;
import br.com.hostel.controller.form.RoomUpdateForm;
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

	public ResponseEntity<RoomDto> registerRoom(RoomForm form, UriComponentsBuilder uriBuilder) {
		Room room = form.returnRoom(dailyRateRepository);

		Optional<Room> roomOp = roomRepository.findByNumber(room.getNumber());

		if (roomOp.isPresent() || room.getNumber() == 0) {
			return ResponseEntity.badRequest().build();
		} else {
			roomRepository.save(room);

			URI uri = uriBuilder.path("/rooms/{id}").buildAndExpand(room.getId()).toUri();
			return ResponseEntity.created(uri).body(new RoomDto(room));
		}
	}

	public ResponseEntity<List<RoomDto>> listAllRooms(String checkinDateString, String checkoutDateString,
			Integer numberOfGuests, Pageable pagination) {

		List<RoomDto> response = new ArrayList<>();
		List<Room> invalidRooms = new ArrayList<>();
		List<Room> validRooms = roomRepository.findAll();
		
		

		if (checkinDateString == null || checkoutDateString == null)
			response = RoomDto.convert(roomRepository.findAll());
		else {
			// convert String to LocalDate
			LocalDate checkinDate = LocalDate.parse(checkinDateString);
			LocalDate checkoutDate = LocalDate.parse(checkoutDateString);

			List<Reservation> reservationsList = reservationRepository.findAll();

			if (numberOfGuests != null) {
				validRooms.forEach(room -> {
					if (room.getMaxNumberOfGuests() < numberOfGuests) {
						invalidRooms.add(room);
					}
				});
			}

			System.out.println("alooo");

			reservationsList.forEach(reservation -> {

				long numOfDays = ChronoUnit.DAYS.between(reservation.getCheckinDate(), reservation.getCheckoutDate());

				List<LocalDate> dates = Stream.iterate(reservation.getCheckinDate(), date -> date.plusDays(1))
						.limit(numOfDays).collect(Collectors.toList());

				if ((dates.contains(checkinDate) || dates.contains(checkoutDate))
						|| (checkinDate.isBefore(reservation.getCheckinDate())
								&& checkoutDate.isAfter(reservation.getCheckoutDate())
								|| checkoutDate.isEqual(reservation.getCheckoutDate()))) {

					reservation.getRooms().forEach(room -> {
						if (!invalidRooms.contains(room)) {
							invalidRooms.add(room);
						}
					});
				}

			});

			invalidRooms.forEach(room -> validRooms.remove(room));
		}
		response = RoomDto.convert(validRooms);

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<RoomDto> listOneRoom(Long id) {
		Optional<Room> room = roomRepository.findById(id);

		if (room.isPresent())
			return ResponseEntity.ok(new RoomDto(room.get()));
		else
			return ResponseEntity.notFound().build();
	}

	public ResponseEntity<RoomDto> updateRoom(@PathVariable Long id, @RequestBody @Valid RoomUpdateForm form,
			UriComponentsBuilder uriBuilder) {

		Optional<Room> roomOp = roomRepository.findById(id);

		if (roomOp.isPresent()) {
			Room room = form.updateRoomForm(id, roomOp.get(), roomRepository);

			dailyRateRepository.save(room.getDailyRate());

			roomRepository.save(room);

			return ResponseEntity.ok(new RoomDto(room));
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> deleteRoom(Long id) {
		Optional<Room> room = roomRepository.findById(id);

		if (room.isPresent()) {
			roomRepository.deleteById(id);

			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.notFound().build();
	}

}
