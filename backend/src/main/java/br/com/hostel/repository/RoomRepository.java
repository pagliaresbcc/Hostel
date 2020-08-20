package br.com.hostel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{

	Optional<Room> findByNumber(Integer number);

}