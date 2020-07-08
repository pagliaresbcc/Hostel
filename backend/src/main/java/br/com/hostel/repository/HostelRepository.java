package br.com.hostel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.Hostel;

public interface HostelRepository extends JpaRepository<Hostel, Long>{
	Optional<Hostel> findByName(String name);

}
