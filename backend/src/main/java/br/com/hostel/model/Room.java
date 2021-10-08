package br.com.hostel.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

	public Room(String description, Integer number, Double dimension, Integer maxNumberOfGuests, DailyRate dailyRate) {
		this.description = description;
		this.number = number;
		this.dimension = dimension;
		this.maxNumberOfGuests = maxNumberOfGuests;
		this.dailyRate = dailyRate;
	}

	public String toString() {
		String resultado = "Room number...: " + this.number + "\n" + "Room dimension (m2)...: " + this.dimension + "\n";
		return resultado;
	}

}
