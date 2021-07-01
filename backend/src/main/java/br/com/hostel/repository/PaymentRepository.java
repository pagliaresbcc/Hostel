package br.com.hostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
