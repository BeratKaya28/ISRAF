package com.israf.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.israf.api.model.Basket;
import com.israf.api.model.User;

public interface BasketRepository extends JpaRepository<Basket, Long>{

	Optional<Basket> findByUser(User user);
	
	
}
