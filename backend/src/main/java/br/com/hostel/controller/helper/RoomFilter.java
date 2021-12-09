package br.com.hostel.controller.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomFilter {

	private String checkinDate;
	private String checkoutDate;
	private Integer numberOfGuests;
	private Double minDailyRate;
	private Double maxDailyRate;
	
}
