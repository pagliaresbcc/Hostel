package br.com.hostel.controller.dto;

import br.com.hostel.model.helper.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDto {

	private String token;
	private String type;
	private Long guest_ID;
	private Role role;

}
