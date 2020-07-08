package br.com.hostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.Payments;

public interface PaymentsRepository extends JpaRepository<Payments, Long>{

}
