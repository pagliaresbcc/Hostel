package br.com.hostel.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Room {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private int number;
	@NotNull
	private double dimension;
	@OneToOne(cascade=CascadeType.REMOVE)
	@JoinColumn(name = "dailyRate_ID", nullable = false)
	private DailyRate dailyRate;
	
	public Room() {
		
	}
	
	public Room(int number, double dimension, DailyRate dailyRate) {
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

	public String toString( ) {
		String resultado = "Room number...: " + this.number + "\n" +
	                                    "Room dimension (m2)...: " + this.dimension + "\n";
		return resultado;
	}

}
