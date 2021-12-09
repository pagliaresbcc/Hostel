package br.com.hostel.controller.form;

import javax.validation.constraints.NotNull;

import br.com.hostel.model.DailyRate;
import br.com.hostel.model.Room;
import br.com.hostel.repository.DailyRateRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

	public Room returnRoom(DailyRateRepository dailyRateRepository) {
		dailyRateRepository.save(dailyRate);
		
		return new Room(description, number, dimension, maxNumberOfGuests, dailyRate);
	}

}
