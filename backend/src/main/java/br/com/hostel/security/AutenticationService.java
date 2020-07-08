package br.com.hostel.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.hostel.model.Customer;
import br.com.hostel.repository.CustomerRepository;

@Service
//UserDetailsService serve para indicar ao spring q essa classe possui a logica de utenticação
public class AutenticationService implements UserDetailsService {

	@Autowired
	CustomerRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Customer> customer = repository.findByEmail(username);

		if (customer.isPresent())
			return customer.get();
		else
			throw new UsernameNotFoundException("Ivalid data");
	}
}
