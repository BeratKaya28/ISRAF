package com.israf.api.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.israf.api.model.Role;
import com.israf.api.model.User;
import com.israf.api.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner{

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public void run(String... args) throws Exception{
		if(!userRepository.existsByUsername("admin")) {
			User admin = new User();
			
			admin.setUsername("admin");
			admin.setEmail("admin@israf.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			
			admin.setFirstName("Berat");
            admin.setLastName("Kaya");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setEmailVerified(true);
            admin.setPhoneNumber("0896 645 99 99 ");
            
            admin.setCity("Giresun");
            admin.setDistrict("Bulancak");
            admin.setAddress("Admin Merkezi");
            admin.setLatitude(0.0);
            admin.setLongitude(0.0);
			
            userRepository.save(admin);
            
            System.out.println("=========================================================");
            System.out.println(">>> [SİSTEM BİLGİSİ] Varsayılan Admin hesabı oluşturuldu!");
            System.out.println(">>> Kullanıcı Adı / Email : admin | admin@israf.com");
            System.out.println(">>> Şifre                 : admin123");
            System.out.println("=========================================================");
		}
		
		
	}
	
	
}
