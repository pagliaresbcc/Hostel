package br.com.hostel.model;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("3")
public class CreditCardPayment extends Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double amount;
	private String issuer;
	private String cardNumber;
	private String nameOnCard;
	private LocalDate expirationDate;
	private String securityCode;
	
	public String toString() {
		return "Payment with credit card...: " +  "\n" +
				super.toString() + "\n" +
				"Issuer...: "+ this.issuer + "\n" +
				"Credit card number...: " + this.cardNumber + "\n" +
				"Name on card...: " + this.nameOnCard + "\n" +
				"Expiration date...: " + this.expirationDate + "\n" +
				"Security code...: " + this.securityCode;
	}
}
