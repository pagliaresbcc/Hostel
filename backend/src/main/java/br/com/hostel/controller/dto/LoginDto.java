package br.com.hostel.controller.dto;

public class LoginDto {

	private String token;
	private String type;
	private Long customer_ID;

	public LoginDto(String token, String tipo, Long customer_ID) {
		this.token = token;
		this.type = tipo;
		this.customer_ID = customer_ID;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

	public Long getCustomer_ID() {
		return customer_ID;
	}
}
