package br.com.hostel.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.dto.RoomDto;
import br.com.hostel.controller.form.RoomForm;
import br.com.hostel.controller.form.RoomUpdateForm;
import br.com.hostel.controller.helper.RoomFilter;
import br.com.hostel.exceptions.BaseException;
import br.com.hostel.model.Room;
import br.com.hostel.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@PostMapping
	public ResponseEntity<?> registerRoom(@RequestBody @Valid RoomForm form, UriComponentsBuilder uriBuilder)
			throws BaseException {

		try {
			Room room = roomService.registerRoom(form, uriBuilder);

			URI uri = uriBuilder.path("/rooms/{id}").buildAndExpand(room.getId()).toUri();

			return ResponseEntity.created(uri).body(new RoomDto(room));

		} catch (BaseException be) {
			return ResponseEntity.status(be.getHttpStatus()).body(be.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<List<RoomDto>> listAllRooms(@RequestParam(required = false) String checkinDate,
			@RequestParam(required = false) String checkoutDate, @RequestParam(required = false) Integer numberOfGuests,
			@RequestParam(required = false) Double minDailyRate, @RequestParam(required = false) Double maxDailyRate,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable pagination) {
		
		List<Room> roomList = this.roomService.listAllRooms(
				new RoomFilter(checkinDate, checkoutDate, numberOfGuests, minDailyRate, maxDailyRate), pagination);
		
		return ResponseEntity.ok(RoomDto.convert(roomList));
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> listOneRoom(@PathVariable Long id) throws BaseException {

		try {
			Room room = this.roomService.listOneRoom(id);

			return ResponseEntity.ok(new RoomDto(room));
		} catch (BaseException be) {
			return ResponseEntity.status(be.getHttpStatus()).body(be.getMessage());
		}
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody @Valid RoomUpdateForm form,
			UriComponentsBuilder uriBuilder) throws BaseException {

		try {
			Room room = this.roomService.updateRoom(id, form, uriBuilder);

			return ResponseEntity.ok(new RoomDto(room));
		} catch (BaseException be) {
			return ResponseEntity.status(be.getHttpStatus()).body(be.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteRoom(@PathVariable Long id) throws BaseException {
		try {
			this.roomService.deleteRoom(id);

			return ResponseEntity.ok().build();
		} catch (BaseException be) {
			return ResponseEntity.status(be.getHttpStatus()).body(be.getMessage());
		}
	}
}
