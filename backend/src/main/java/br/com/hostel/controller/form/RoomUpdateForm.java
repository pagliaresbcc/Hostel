package br.com.hostel.controller.form;

import java.util.Optional;

import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.RoomRepository;

public class RoomUpdateForm {
	
	private String description;
	private int number;
	private double dimension;
	private DailyRate dailyRate;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getDimension() {
		return dimension;
	}

	public void setDimension(double dimension) {
		this.dimension = dimension;
	}

	public DailyRate getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(DailyRate dailyRate) {
		this.dailyRate = dailyRate;
	}
	
	public Room updateRoomForm(Long id, Room room, RoomRepository roomRepository) {
		verifyIfParamIsNotNull(room, roomRepository);
		return room;
	}
	
	private void verifyIfParamIsNotNull(Room room, RoomRepository roomRepository) {
		
		if (description != null)
			room.setDescription(description);

		if (number != 0) {
			Optional<Room> roomOp = roomRepository.findByNumber(number);

			if (!roomOp.isPresent()) {
				room.setNumber(number);
			}
		}

		if (dimension != 0)
			room.setDimension(dimension);
		
		if (dailyRate != null)
			room.setDailyRate(dailyRate);
	}
}
