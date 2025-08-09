package com.java.healSync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.enums.Role;
import com.java.healSync.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
	Optional<User> findByEmailAndPasswordAndRole(String email, String password, Role role);
	
	boolean existsByRole(Role role);
}
