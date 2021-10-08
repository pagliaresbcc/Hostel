package br.com.hostel.model;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String addressName;
	@Column(nullable=false)
	private String zipCode;
	@Column(nullable=false)
	private String city;
	@Column(nullable=false)
	private String state;
	@Column(nullable=false)
	private String country;	
	 
	public Address(String addressName, String zipCode, String city, String state, String country) {
		super();
		this.addressName = addressName;
		this.zipCode = zipCode;
		this.city = city;
		this.state = state;
		this.country = country;
	}

}
