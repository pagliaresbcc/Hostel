package br.com.hostel.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("1")
public class CashPayment extends Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double amountTendered;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public double getAmountTendered() {
		return amountTendered;
	}

	public void setAmountTendered(double amountTendered) {
		this.amountTendered = amountTendered;
	}
}
