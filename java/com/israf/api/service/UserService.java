package com.israf.api.service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.israf.api.dto.LoginRequestDto;
import com.israf.api.dto.LoginResponseDto;
import com.israf.api.dto.SellerDto;
import com.israf.api.dto.StoreDto;
import com.israf.api.dto.UserProfileDto;
import com.israf.api.dto.UserRequestDto;
import com.israf.api.model.Role;
import com.israf.api.model.Store;
import com.israf.api.model.User;
import com.israf.api.repository.StoreRepository;
import com.israf.api.repository.UserRepository;
import com.israf.api.security.JwtService;

import org.springframework.transaction.annotation.Transactional;
@Service
public class UserService implements UserDetailsService{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final StoreRepository storeRepository; 
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final EmailService emailService;
	
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, StoreRepository storeRepository, JwtService jwtService, @Lazy AuthenticationManager authenticationManager, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.storeRepository = storeRepository;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.emailService = emailService;
	}
	
	@Transactional
	public UserProfileDto registerSeller(SellerDto dto) {
		if(userRepository.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("Email already exists.");
		}
		if(userRepository.existsByUsername(dto.getUsername())) {
			throw new RuntimeException("Username already exists");
		}
		
		User user = new User();
		String vCode = emailService.generateVerificationCode();
		user.setVerificationCode(vCode);
		user.setEmailVerified(false);
		
		user.setEmail(dto.getEmail());
		user.setUsername(dto.getUsername());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setLastName(dto.getLastName());
		user.setFirstName(dto.getFirstName());
		user.setPhoneNumber(dto.getPhoneNumber());
		user.setCity(dto.getCity());
		user.setDistrict(dto.getDistrict());
		user.setLongitude(dto.getLongitude());
		user.setLatitude(dto.getLatitude());
		user.setFormattedAddress(dto.getFormattedAddress());
		user.setRole(Role.SELLER);
		user.setActive(true);
		user.setAddress(dto.getAddress());
		user.setProfilePicture(dto.getProfilePictureUrl());
		
		User savedUser = userRepository.save(user);
			
		Store store = new Store();
		store.setStoreName(dto.getStoreName());
		store.setStoreType(dto.getStoreType());
		store.setPhoneNumber(dto.getStorePhoneNumber());
		store.setAddress(dto.getStoreAddress());
		store.setCity(dto.getStoreCity());
		store.setDistrict(dto.getStoreDistrict());
		store.setLatitude(dto.getStoreLatitude());
		store.setLongitude(dto.getStoreLongitude());
		store.setFormattedAddress(dto.getStoreFormattedAddress());
		store.setOwner(savedUser);
		store.setStoreImageUrls(dto.getStoreImageUrls());
		
		Store savedStore = storeRepository.save(store);
		savedUser.setStores(List.of(savedStore));
		
		emailService.sendVerificationEmail(savedUser.getEmail(), vCode);
		
		return mapToUserProfileDto(savedUser);
	}
	
	@Transactional
	public LoginResponseDto adminManualLogin(LoginRequestDto dto) {
	    Authentication auth = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(dto.getUsernameOrEmail(), dto.getPassword())
	    );
	    
	    User admin = userRepository.findByUsernameOrEmail(dto.getUsernameOrEmail(), dto.getUsernameOrEmail())
	            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
	            
	    if (admin.getRole() != Role.ADMIN) {
	        throw new RuntimeException("Bu panele sadece yöneticiler giriş yapabilir!");
	    }
	    
	    String token = jwtService.generateToken(admin);
	    UserProfileDto profile = mapToUserProfileDto(admin);
	    
	    return new LoginResponseDto(profile, token);
	}
	
	@Transactional
	public UserProfileDto completeSellerProfile(SellerDto dto) {
		User user = getCurrentUser();
				
		if(dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
			if(userRepository.existsByUsername(dto.getUsername())) {
				throw new RuntimeException("Username is already exists.");
			}
			user.setUsername(dto.getUsername());
		}
		
		user.setFirstName(dto.getFirstName());
	    user.setLastName(dto.getLastName());
	    user.setPhoneNumber(dto.getPhoneNumber());
	    user.setCity(dto.getCity());
	    user.setDistrict(dto.getDistrict());
	    user.setAddress(dto.getAddress());
	    user.setFormattedAddress(dto.getFormattedAddress());
	    user.setLatitude(dto.getLatitude());
	    user.setLongitude(dto.getLongitude());
	    user.setRole(Role.SELLER);
	    user.setProfilePicture(dto.getProfilePictureUrl());
	    
	    Store store = new Store();
        store.setStoreName(dto.getStoreName());
        store.setStoreType(dto.getStoreType());
        store.setPhoneNumber(dto.getStorePhoneNumber()); 
        store.setAddress(dto.getStoreAddress());         
        store.setFormattedAddress(dto.getStoreFormattedAddress());
        store.setDistrict(dto.getStoreDistrict());
        store.setCity(dto.getStoreCity());
        store.setLatitude(dto.getStoreLatitude());
        store.setLongitude(dto.getStoreLongitude());
        store.setOwner(user);
        store.setStoreImageUrls(dto.getStoreImageUrls());
        
        
        Store savedStore = storeRepository.saveAndFlush(store);
        user.setStores(new ArrayList<>(List.of(savedStore)));
        User savedUser = userRepository.saveAndFlush(user);
        
        return mapToUserProfileDto(savedUser);
     
	}
	
	@Transactional
	public UserProfileDto registerCustomer(UserRequestDto dto) {
		User user;
		
		Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
		
		if(existingUser.isPresent()) {
			user = existingUser.get();
			
			if(user.getPassword() != null && user.getPassword().isEmpty()) {
				throw new RuntimeException("Email already exists.");
			}
			
			if(!user.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
				throw new RuntimeException("Username already exists.");
			}
		}else {
			if(userRepository.existsByUsername(dto.getUsername())) {
				throw new RuntimeException("Username already exists.");
			}
			
			user = new User();
			user.setEmail(dto.getEmail());
			user.setRole(Role.CUSTOMER);
			user.setActive(true);
		}
		
		user.setUsername(dto.getUsername());
	    user.setPassword(passwordEncoder.encode(dto.getPassword())); 
	    user.setFirstName(dto.getFirstName());
	    user.setLastName(dto.getLastName());
	    user.setPhoneNumber(dto.getPhoneNumber());
	    user.setCity(dto.getCity());
	    user.setDistrict(dto.getDistrict());
	    user.setLatitude(dto.getLatitude());
	    user.setLongitude(dto.getLongitude());
	    user.setAddress(dto.getAddress());
	    user.setFormattedAddress(dto.getFormattedAddress());
		
	    if (dto.getProfilePictureUrl() != null) {
	        user.setProfilePicture(dto.getProfilePictureUrl());
	    }
	    
	    boolean needsVerificationEmail = false;
	    if (!user.isEmailVerified()) {
	        String vCode = emailService.generateVerificationCode();
	        user.setVerificationCode(vCode);
	        user.setEmailVerified(false);
	        needsVerificationEmail = true;
	    }
	    
	    User savedUser = userRepository.save(user);
	    
	    if (needsVerificationEmail) {
	        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationCode());
	    }

	    return mapToUserProfileDto(savedUser);
	}
	
	@Transactional
	public LoginResponseDto login(LoginRequestDto dto) {
		
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsernameOrEmail(), dto.getPassword()));
		
		User user = userRepository.findByUsernameOrEmail(dto.getUsernameOrEmail(), dto.getUsernameOrEmail()).orElseThrow(() -> new RuntimeException("User not found"));
		
		if(!user.isActive()) {
			throw new RuntimeException("Hesabiniz askiya alindi");
		}
		
		
		String token = jwtService.generateToken(user);
		
		UserProfileDto profile = mapToUserProfileDto(user);
		
		return new LoginResponseDto(profile, token);
		
	}
	
	@Transactional
	public void forgotPassword(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("There are no users registered with this email address."));
		
		String resetCode = emailService.generateVerificationCode();
		user.setResetPasswordCode(resetCode);
		
		user.setResetCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
		
		userRepository.save(user);
		emailService.sendPasswordResetEmail(email, resetCode);
	}
	
	@Transactional
	public void resetPassword(String email, String code, String newPassword) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found."));
		
		if(user.getResetPasswordCode() == null || !user.getResetPasswordCode().equals(code)) {
			throw new RuntimeException("Gecersiz sifre sifirlama kodu.");
		}
		
		if(user.getResetCodeExpiresAt().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Sifre sifirlama kodunun suresi dolmus. Lutfen tekrar talep edin.");
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		
		user.setResetPasswordCode(null);
		user.setResetCodeExpiresAt(null);
		userRepository.save(user);
	}
	
	
	
	@Transactional
	public UserProfileDto updateUserInfo(UserProfileDto dto) {
		User user = getCurrentUser();
		
		if(dto.getFirstName() != null) {
			user.setFirstName(dto.getFirstName());
		}
		if(dto.getLastName() != null) {
			user.setLastName(dto.getLastName());
		}
		if(dto.getAddress() != null) {
			user.setAddress(dto.getAddress());
		}
		if(dto.getPhoneNumber() != null) {
			user.setPhoneNumber(dto.getPhoneNumber());
		}
		if(dto.getLatitude() != null) {
			user.setLatitude(dto.getLatitude());
		}
		if(dto.getLongitude() != null) {
			user.setLongitude(dto.getLongitude());
		}
		if(dto.getCity() != null) {
			user.setCity(dto.getCity());
		}
		if(dto.getDistrict() != null) {
			user.setDistrict(dto.getDistrict());
		}
		if(dto.getFormattedAddress() != null) {
			user.setFormattedAddress(dto.getFormattedAddress());
		}
		
		if(dto.getProfilePictureUrl() != null) {
			user.setProfilePicture(dto.getProfilePictureUrl());
		}
		
		if(dto.getProfilePictureUrl() != null) {
			user.setProfilePicture(dto.getProfilePictureUrl());
		}
		
		
	User savedUser = userRepository.save(user);
	return mapToUserProfileDto(savedUser);
	}
	
	@Transactional
	public void fastUpdateLocation(Double longitude, Double latitude, String formattedAddress) {
		User user = getCurrentUser();
		
		user.setLatitude(latitude);
		user.setLongitude(longitude);
		
		if(formattedAddress != null) {
			user.setFormattedAddress(formattedAddress);
		}
		userRepository.save(user);
	}
	
	@Transactional(readOnly = true)
	public UserProfileDto getSellerProfile(String username) {
		User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new RuntimeException("Seller not found"));
		
		if(user.getRole() != Role.SELLER) {
			throw new RuntimeException("User is not a seller");
		}
		
		if(!user.isActive()) {
			throw new RuntimeException("Seller account is not active");
		}
		return mapToUserProfileDto(user);
	}
	
	@Transactional
	public void changePassword(String oldPassword, String newPassword) {
		User user = getCurrentUser();
		
		if(user.getPassword() == null || user.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return;
		}
		
		if(oldPassword == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect");
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}
	
	@Transactional 
	public void changeUsername(String newUsername) {
		User user = getCurrentUser();
		
		if(userRepository.existsByUsername(newUsername)) {
			throw new IllegalArgumentException("Username already exists.");
		}
		
		user.setUsername(newUsername);
		userRepository.save(user);
	}
	
	@Transactional
	public void changeEmail(String newEmail) {
		User user = getCurrentUser();
		
		if(userRepository.existsByEmail(newEmail)) {
			throw new IllegalArgumentException("Email already exists.");
		}
		
		user.setEmail(newEmail);
		user.setEmailVerified(false);
		
		String vCode = emailService.generateVerificationCode();
		user.setVerificationCode(vCode);
		
		userRepository.save(user);
		
		emailService.sendVerificationEmail(newEmail, vCode);
	}
	
	@Transactional
	public boolean verifyEmail(String email, String code) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		
		if(user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
			user.setEmailVerified(true);
			user.setVerificationCode(null);
			userRepository.save(user);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly = true)
	public UserProfileDto getMyProfileDto() {
		User user = getCurrentUser();
		return mapToUserProfileDto(user);
	}
	
	@Transactional
	public void deleteAccount() {
		User user = getCurrentUser();
		user.setActive(false);
		userRepository.save(user);
	}
	
	@Transactional
	public LoginResponseDto handleGoogleLogin(Map<String, String> googleData) {
		String email = googleData.get("email");
		Optional<User> existingUser = userRepository.findByEmail(email);
		
		if(existingUser.isPresent()) {
			String token = jwtService.generateToken(existingUser.get());
			UserProfileDto profile = mapToUserProfileDto(existingUser.get());
			return new LoginResponseDto(profile, token);
		}else {
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setUsername(email); 
			newUser.setFirstName(googleData.get("firstName"));
			newUser.setLastName(googleData.get("lastName"));
			newUser.setRole(Role.CUSTOMER);
			newUser.setActive(true);
			newUser.setEmailVerified(true); 
			
			userRepository.save(newUser);
			return null;
		}
		
		
	}
	
	public UserProfileDto mapToUserProfileDto(User user) {	
		
		UserProfileDto dto = new UserProfileDto();
		
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setUsername(user.getUsername());
		dto.setRole(user.getRole().name());
		dto.setLastName(user.getLastName());
		dto.setFirstName(user.getFirstName());
		dto.setDistrict(user.getDistrict());
		dto.setCity(user.getCity());
		dto.setPhoneNumber(user.getPhoneNumber());
		dto.setAddress(user.getAddress());
		dto.setFormattedAddress(user.getFormattedAddress());
		dto.setLongitude(user.getLongitude());
		dto.setLatitude(user.getLatitude());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setProfilePictureUrl(user.getProfilePicture());
		dto.setActive(user.isActive());
		dto.setHasPassword(user.getPassword() != null && !user.getPassword().isEmpty());
		
		if(user.getRole() == Role.SELLER && user.getStores() != null) {
			List<StoreDto> dtos = user.getStores().stream()
					.filter(store -> store.isActive() != null && store.isActive())
					.map(
					store -> {
						StoreDto storeDto = new StoreDto();
						storeDto.setId(store.getId());
						storeDto.setStoreName(store.getStoreName());
						storeDto.setStoreType(store.getStoreType());
						storeDto.setFormattedAddress(store.getFormattedAddress());
						storeDto.setAddress(store.getAddress());
						storeDto.setStorePhoneNumber(store.getPhoneNumber());
						storeDto.setLatitude(store.getLatitude());
						storeDto.setLongitude(store.getLongitude());
						storeDto.setDistrict(store.getDistrict());
						storeDto.setCity(store.getCity());
						storeDto.setStoreImageUrls(store.getStoreImageUrls());
						return storeDto;
					}).collect(java.util.stream.Collectors.toList());
			
			dto.setStores(dtos);
		}
		
		return dto;
		
	}
	
	@Transactional(readOnly = true)
	public User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !(auth.getPrincipal() instanceof User)) {
			throw new RuntimeException("Kullanici bulunamadi veya oturum geçersiz.");
		}
		User authUser = (User) auth.getPrincipal();
		return userRepository.findById(authUser.getId())
				.orElseThrow(() -> new RuntimeException("Veritabanında kullanıcı bulunamadı."));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException("User not found: "+username));
	}

	
	
}
