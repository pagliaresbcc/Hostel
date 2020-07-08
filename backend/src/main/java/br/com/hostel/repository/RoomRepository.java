package br.com.hostel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hostel.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{

	List<Room> findByNumber(int number);
	

}

