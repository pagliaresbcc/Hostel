package br.com.hostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
