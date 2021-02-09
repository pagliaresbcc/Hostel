package br.com.hostel.model;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Address
 *
 */
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
	
	public Address() {
		
	}
	 
	public Address(String addressName, String zipCode, String city, String state, String country) {
		super();
		this.addressName = addressName;
		this.zipCode = zipCode;
		this.city = city;
		this.state = state;
		this.country = country;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long addressID) {
		this.id = addressID;
	}   
	
	public String getAddressName() {
		return this.addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}   
	
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}   
	
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}   
	
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}   
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
