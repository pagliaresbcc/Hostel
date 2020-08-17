package br.com.hostel.controller.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;

public class RoomDto {

	private Long id;
	private String description;
	private Integer number;
	private Double dimension;
	private Integer maxNumberOfGuests;
	private DailyRate dailyRate;

	public RoomDto() {
	}

	public RoomDto(Room room) {
		this.id = room.getId();
		this.description = room.getDescription();
		this.number = room.getNumber();
		this.dimension = room.getDimension();
		this.maxNumberOfGuests = room.getMaxNumberOfGuests();
		this.dailyRate = room.getDailyRate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public Double getDimension() {
		return dimension;
	}

	public DailyRate getDailyRate() {
		return dailyRate;
	}

	public String getDescription() {
		return description;
	}

	public Integer getMaxNumberOfGuests() {
		return maxNumberOfGuests;
	}

	public static List<RoomDto> convert(List<Room> roomsList) {

		List<RoomDto> roomsDtoList = new ArrayList<>();

		roomsList.forEach(room -> roomsDtoList.add(new RoomDto(room)));

		return roomsDtoList;
	}

}
