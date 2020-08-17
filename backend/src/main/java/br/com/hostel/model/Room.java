package br.com.hostel.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String description;
	@Column(nullable = false)
	private Integer number;
	@Column(nullable = false)
	private Double dimension;
	@Column(nullable = false)
	private Integer maxNumberOfGuests;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "dailyRate_ID", nullable = false)
	private DailyRate dailyRate;

	public Room() {

	}

	public Room(String description, Integer number, Double dimension, Integer maxNumberOfGuests, DailyRate dailyRate) {
		this.description = description;
		this.number = number;
		this.dimension = dimension;
		this.maxNumberOfGuests = maxNumberOfGuests;
		this.dailyRate = dailyRate;
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

	public String toString() {
		String resultado = "Room number...: " + this.number + "\n" + "Room dimension (m2)...: " + this.dimension + "\n";
		return resultado;
	}

}
