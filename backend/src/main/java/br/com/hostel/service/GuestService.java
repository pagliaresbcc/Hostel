package br.com.hostel.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hostel.controller.form.GuestForm;
import br.com.hostel.controller.form.GuestUpdateForm;
import br.com.hostel.exceptions.BaseException;
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

	public Guest createGuest(GuestForm form, UriComponentsBuilder uriBuilder) throws BaseException {
		Optional<Guest> guestEmail = guestRepository.findByEmail(form.getEmail());

		if (guestEmail.isPresent())
			throw new BaseException("There is already a guest with e-mail = " + form.getEmail(),
					HttpStatus.BAD_REQUEST);

		Guest guest = form.returnGuest(addressRepository);

		return guestRepository.save(guest);

	}

	public List<Guest> listAllGuests(String name, Pageable pagination) {

		if (name == null)
			return guestRepository.findAll();

		return guestRepository.findByName(name);
	}

	public Guest listOneGuest(Long id) throws BaseException {

		Optional<Guest> guest = guestRepository.findById(id);

		if (!guest.isPresent())
			throw new BaseException("There isn't a guest with id = " + id, HttpStatus.NOT_FOUND);

		return guest.get();
	}

	public List<Reservation> listGuestReservations(Long guest_ID) throws BaseException {

		Optional<Guest> guest = guestRepository.findById(guest_ID);

		if (!guest.isPresent())
			throw new BaseException("There isn't a guest with id = " + guest_ID, HttpStatus.NOT_FOUND);

		List<Reservation> reservations = guest.get().getReservations().stream().collect(Collectors.toList());

		Collections.sort(reservations);

		return reservations;
	}

	public Guest updateGuest(Long id, @Valid GuestUpdateForm form) throws BaseException {
		Optional<Guest> guestOp = guestRepository.findById(id);

		if (!guestOp.isPresent())
			throw new BaseException("There isn't a guest with id = " + id, HttpStatus.NOT_FOUND);

		Guest guest = form.updateGuestForm(id, guestOp.get(), guestRepository);
		addressRepository.save(guest.getAddress());

		return guest;
	}

	public void deleteGuest(Long id) throws BaseException {

		Optional<Guest> guest = guestRepository.findById(id);

		if (!guest.isPresent())
			throw new BaseException("There isn't a guest with id = " + id, HttpStatus.NOT_FOUND);

		guestRepository.deleteById(id);
	}
}
