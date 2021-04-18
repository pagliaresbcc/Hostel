package br.com.hostel.controller.helper;

public class RoomFilter {

	private String checkinDate;
	private String checkoutDate;
	private Integer numberOfGuests;
	private Double minDailyRate;
	private Double maxDailyRate;
	
	public RoomFilter() {
		
	}

	public RoomFilter(String checkinDate, String checkoutDate, Integer numberOfGuests, Double minDailyRate,
			Double maxDailyRate) {
		super();
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.numberOfGuests = numberOfGuests;
		this.minDailyRate = minDailyRate;
		this.maxDailyRate = maxDailyRate;
	}

	public String getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(Integer numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public Double getMinDailyRate() {
		return minDailyRate;
	}

	public void setMinDailyRate(Double minDailyRate) {
		this.minDailyRate = minDailyRate;
	}

	public Double getMaxDailyRate() {
		return maxDailyRate;
	}

	public void setMaxDailyRate(Double maxDailyRate) {
		this.maxDailyRate = maxDailyRate;
	}

}
