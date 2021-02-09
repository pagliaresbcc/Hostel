package br.com.hostel.model;

import java.lang.String;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.hostel.model.helper.Role;

@Entity
//UserDetails serve para dizer ao Sring qual será a classe de usuário
//para autenticação no sistema
public class Guest implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String lastName;
	@Column(nullable = false)
	private LocalDate birthday;
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "address_ID", nullable = false)
	private Address address;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	
	@Column(columnDefinition = "ENUM('ROLE_USER', 'ROLE_ADMIN')")
    @Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "perfis")
	@OneToMany(fetch = FetchType.EAGER)
	private List<Perfil> perfis = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.REMOVE, CascadeType.MERGE })
	@Column(name = "reservations")
	private Set<Reservation> reservations = new HashSet<>();

	public Guest() {

	}

	public Guest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Guest(String title, String name, String lastName, LocalDate birthday, Address address, String email,
			String password, Role role) {
		this.title = title;
		this.name = name;
		this.lastName = lastName;
		this.birthday = birthday;
		this.address = address;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Guest other = (Guest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthday() {
		return this.birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Reservation> getReservations() {
		return this.reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public void addReservation(Reservation reservation) {
		this.reservations.add(reservation);
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
