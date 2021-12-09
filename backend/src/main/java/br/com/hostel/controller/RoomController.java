package br.com.hostel.controller;

import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.RoomForm;
import br.com.hostel.controller.form.RoomUpdateForm;
import br.com.hostel.controller.helper.RoomFilter;
import br.com.hostel.exceptions.room.RoomException;
import br.com.hostel.model.Room;
import br.com.hostel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@PostMapping
	public ResponseEntity<RoomDto> registerRoom(@RequestBody @Valid RoomForm form, UriComponentsBuilder uriBuilder)
			throws RoomException {

		Room room = roomService.registerRoom(form);

		URI uri = uriBuilder.path("/rooms/{id}").buildAndExpand(room.getId()).toUri();

		return ResponseEntity.created(uri).body(new RoomDto(room));
	}

	@GetMapping
	public ResponseEntity<List<RoomDto>> listAllRooms(@RequestParam(required = false) String checkinDate,
			@RequestParam(required = false) String checkoutDate, @RequestParam(required = false) Integer numberOfGuests,
			@RequestParam(required = false) Double minDailyRate, @RequestParam(required = false) Double maxDailyRate) {
		
		List<Room> roomList = this.roomService.listAllRooms(
				new RoomFilter(checkinDate, checkoutDate, numberOfGuests, minDailyRate, maxDailyRate));
		
		return ResponseEntity.ok(RoomDto.convert(roomList));
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoomDto> listOneRoom(@PathVariable Long id) throws RoomException {

		Room room = roomService.listOneRoom(id);

		return ResponseEntity.ok(new RoomDto(room));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<RoomDto> updateRoom(@PathVariable Long id, @RequestBody @Valid RoomUpdateForm form)
			throws RoomException {

		Room room = roomService.updateRoom(id, form);

		return ResponseEntity.ok(new RoomDto(room));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Object> deleteRoom(@PathVariable Long id) throws RoomException {
		roomService.deleteRoom(id);

		return ResponseEntity.ok().build();
	}
}
