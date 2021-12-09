package br.com.hostel.controller.form;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.helper.Role;
import br.com.hostel.repository.AddressRepository;

@Getter
@Setter
public class GuestForm {
	
	@NotNull 
	private String title;
	@NotNull 
	private String name;
	@NotNull 
	private String lastName;
	@NotNull 
	private LocalDate birthday;
	@NotNull 
	private Address address;
	@NotNull 
	private String email;
	@NotNull 
	private String password;
	@NotNull
	private Role role;
	
	public Guest returnGuest(AddressRepository addressRepository) {
		addressRepository.save(address);
		
		return new Guest(title, name, lastName, birthday, address, email,
				new BCryptPasswordEncoder().encode(password), role);
	}
}
