package br.com.hostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.CheckinCheckoutDates;

public interface CheckInCheckOutDateRepository extends JpaRepository<CheckinCheckoutDates, Long> {

}
