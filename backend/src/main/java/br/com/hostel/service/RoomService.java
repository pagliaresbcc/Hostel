package br.com.hostel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.RoomForm;
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
		roomRepository.save(room);

		URI uri = uriBuilder.path("/rooms/{id}").buildAndExpand(room.getId()).toUri();
		return ResponseEntity.created(uri).body(new RoomDto(room));
	}

	public ResponseEntity<List<RoomDto>> listAllRooms(Integer number, Pageable pagination) {

		List<RoomDto> response = new ArrayList<>();

		if (!(number instanceof Integer))
			response = RoomDto.convert(roomRepository.findAll());
		else
			response = RoomDto.convert(roomRepository.findByNumber(number));

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

	public ResponseEntity<?> deleteRoom(Long id) {
		Optional<Room> room = roomRepository.findById(id);
		if (room.isPresent()) {
			roomRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.notFound().build();
	}
	
	
}
