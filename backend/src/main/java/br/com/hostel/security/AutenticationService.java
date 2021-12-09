package br.com.hostel.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.hostel.model.Guest;
import br.com.hostel.repository.GuestRepository;

@Service
public class AutenticationService implements UserDetailsService {

	@Autowired
	GuestRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Guest> guest = repository.findByEmail(username);

		if (guest.isPresent())
			return guest.get();
		else
			throw new UsernameNotFoundException("Ivalid data");
	}
}
