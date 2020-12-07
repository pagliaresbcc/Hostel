package br.com.hostel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long>{

	List<Guest> findByName(String nome);
	
	Optional<Guest> findByEmail(String email);
	
	Address findByAddressId(Long id);	
	
}
