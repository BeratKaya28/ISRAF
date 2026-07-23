package com.israf.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.israf.api.model.Role;
import com.israf.api.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByUsernameOrEmail(String username, String email);
	List<User> findByRole(Role role);
	List<User> findByIsActiveFalse();
	List<User> findByCityAndDistrictAndRoleAndIsActiveTrue(String city, String district, Role role);
	
}
