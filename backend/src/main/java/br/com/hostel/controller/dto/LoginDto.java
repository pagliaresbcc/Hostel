package br.com.hostel.controller.dto;

public class LoginDto {

	private String token;
	private String type;

	public LoginDto(String token, String tipo) {
		this.token = token;
		this.type = tipo;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}
}
