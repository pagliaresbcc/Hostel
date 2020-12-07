package br.com.hostel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.Address;
import br.com.hostel.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	//como curso é outra tabela, se fizer concatenado findByTABELAATRIBUTO
	//o spring ja gera a query automatico 
	
	//se houver uma variavel customerNome, para pegar a variavel nome dentro de Curso
	//seria findByCustomer_Nome
	List<Customer> findByName(String nome);
	
	Optional<Customer> findByEmail(String email);
	
	Address findByAddressId(Long id);	
	
}
