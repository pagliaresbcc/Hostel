package br.com.hostel.controller.dto;

import br.com.hostel.model.Role;

public class LoginDto {

	private String token;
	private String type;
	private Long guest_ID;
	private Role role;

	public LoginDto(String token, String tipo, Long guest_ID, Role role) {
		this.token = token;
		this.type = tipo;
		this.guest_ID = guest_ID;
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

	public Long getGuest_ID() {
		return guest_ID;
	}

	public Role getRole() {
		return role;
	}	
	
}
