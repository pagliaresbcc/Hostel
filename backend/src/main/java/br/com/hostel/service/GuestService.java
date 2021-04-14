package br.com.hostel.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.form.GuestForm;
import br.com.hostel.controller.form.GuestUpdateForm;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Reservation;
import br.com.hostel.repository.AddressRepository;
import br.com.hostel.repository.GuestRepository;

@Service
public class GuestService {
	
	@Autowired
	private GuestRepository guestRepository;

	@Autowired
	private AddressRepository addressRepository;

	public Guest createGuest(GuestForm form, UriComponentsBuilder uriBuilder) {
		Optional<Guest> guestEmail = guestRepository.findByEmail(form.getEmail());
		
		if(!guestEmail.isPresent()) {
			
			Guest guest = form.returnGuest(addressRepository);
			
			return guestRepository.save(guest);
		}  
		
		return null;
	}

	public List<Guest> listAllGuests(String name, Pageable pagination) {
		
		if (name == null)
			return guestRepository.findAll();
		 
		return guestRepository.findByName(name);
	}

	public Guest listOneGuest(Long id) {
		
		Optional<Guest> guest = guestRepository.findById(id);
		
		if (guest.isPresent()) return guest.get();
		
		return null;
	}
	
	public List<Reservation> listGuestReservations(Long guest_ID) {

		Optional<Guest> guest = guestRepository.findById(guest_ID);
		
		if (guest.isPresent()) {
			List<Reservation> reservations = guest.get().getReservations().stream().collect(Collectors.toList());
			
			Collections.sort(reservations);
			
			return reservations;
			
		} 
		
		return null;
	}

	public Guest updateGuest(Long id, @Valid GuestUpdateForm form) {
		Optional<Guest> guestOp = guestRepository.findById(id);

		if (guestOp.isPresent()) {
			
			Guest guest = form.updateGuestForm(id, guestOp.get(), guestRepository);
			addressRepository.save(guest.getAddress());

			return guest;
		}  
		
		return null;
	}
	
	public Boolean deleteGuest(Long id) {
		
		Optional<Guest> guest = guestRepository.findById(id);
		
		if (guest.isPresent()) {
			guestRepository.deleteById(id);
			return true;
		} 
			
		return false;
	}
	
}
