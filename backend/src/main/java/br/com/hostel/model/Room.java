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
	
	@Column(nullable=false)
	private String description;
	@Column(nullable=false)
	private int number;
	@Column(nullable=false)
	private double dimension;
	@OneToOne(cascade=CascadeType.REMOVE)
	@JoinColumn(name = "dailyRate_ID", nullable = false)
	private DailyRate dailyRate;
	
	public Room() {
		
	}
	
	public Room(String description, int number, double dimension, DailyRate dailyRate) {
		this.description = description;
		this.number = number;
		this.dimension = dimension;
		this.dailyRate = dailyRate;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString( ) {
		String resultado = "Room number...: " + this.number + "\n" +
	                                    "Room dimension (m2)...: " + this.dimension + "\n";
		return resultado;
	}
}
