package com.israf.api.model;

import java.time.LocalDateTime;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = true)
	private String password;
	
	@Column(nullable = false, unique=true)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@Column
	private String firstName;
	
	@Column 
	private String lastName;
	
	@Column
	private String address;
	
	@Column(nullable = true)
	private String city;
	
	@Column(nullable = true)
	private String district;
	
	@Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String formattedAddress;
    
    @Column(unique = true, nullable = true)
    private String phoneNumber;
    
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
	
	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Store> stores;
	
	@Column
	private boolean isActive = true;
	
	@OneToMany(mappedBy = "customer")
	private List<Order> orders;
	
	private String profilePicture;
	
	@Column(nullable = false)
	private boolean isEmailVerified = false;
	
	@Column
	private String verificationCode;
	
	@Column
	private String resetPasswordCode;
	
	@Column 
	private LocalDateTime resetCodeExpiresAt;
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
	
	
	
	
	public String getResetPasswordCode() {
		return resetPasswordCode;
	}
	
	public void setResetPasswordCode(String resertPasswordCode) {
		this.resetPasswordCode = resertPasswordCode;
	}
	
	public LocalDateTime getResetCodeExpiresAt() {
		return resetCodeExpiresAt;
	}

	public void setResetCodeExpiresAt(LocalDateTime resetCodeExpiresAt) {
		this.resetCodeExpiresAt = resetCodeExpiresAt;
	}

	public String getProfilePicture() {
		return profilePicture;
	}


	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public List<Order> getOrders() {
		return orders;
	}


	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	public boolean isEmailVerified() {
		return isEmailVerified;
	}


	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}


	public String getVerificationCode() {
		return verificationCode;
	}


	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_"+role.name()));
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
		return isActive;
	}
	
	

}
