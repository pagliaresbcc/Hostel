package br.com.hostel.controller.form;

import javax.validation.constraints.NotNull;

import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;

public class RoomForm {

	@NotNull
	private
	String description;
	
	@NotNull
	int number;
	
	@NotNull
	double dimension;
	
	@NotNull
	private	DailyRate dailyRate;

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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Room returnRoom(DailyRateRepository dailyRateRepository) {
		dailyRateRepository.save(dailyRate);
		return new Room(description, number, dimension, dailyRate);
	}
}
