package br.com.hostel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import br.com.hostel.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private DailyRateRepository dailyRateRepository;

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

	public ResponseEntity<List<RoomDto>> listAllRooms(Pageable pagination) {

		List<RoomDto> response = new ArrayList<>();

		response = RoomDto.convert(roomRepository.findAll());

		if (response.isEmpty() || response == null)
			return ResponseEntity.notFound().build();
		else
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
