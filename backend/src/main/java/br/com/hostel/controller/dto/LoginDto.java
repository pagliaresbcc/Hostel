package br.com.hostel.controller.dto;

import br.com.hostel.model.Role;

public class LoginDto {

	private String token;
	private String type;
	private Long customer_ID;
	private Role role;

	public LoginDto(String token, String tipo, Long customer_ID, Role role) {
		this.token = token;
		this.type = tipo;
		this.customer_ID = customer_ID;
		this.role = role;
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

	public Role getRole() {
		return role;
	}	
	
}
