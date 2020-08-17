package br.com.hostel.controller.form;

import javax.validation.constraints.NotNull;

import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;

public class RoomForm {

	@NotNull
	private String description;
	
	@NotNull
	private Integer number;
	
	@NotNull
	private Double dimension;
	
	@NotNull
	private Integer maxNumberOfGuests;
	
	@NotNull
	private	DailyRate dailyRate;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Double getDimension() {
		return dimension;
	}

	public void setDimension(Double dimension) {
		this.dimension = dimension;
	}
	
	public DailyRate getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(DailyRate dailyRate) {
		this.dailyRate = dailyRate;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getMaxNumberOfGuests() {
		return maxNumberOfGuests;
	}
	
	public void setMaxNumberOfGuests(Integer maxNumberOfGuests) {
		this.maxNumberOfGuests = maxNumberOfGuests;
	}
	
	public Room returnRoom(DailyRateRepository dailyRateRepository) {
		dailyRateRepository.save(dailyRate);
		
		return new Room(description, number, dimension, maxNumberOfGuests, dailyRate);
	}

}
