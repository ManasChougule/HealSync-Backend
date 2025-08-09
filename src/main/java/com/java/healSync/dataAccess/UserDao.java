package com.java.healSync.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entities.Role;
import com.java.healSync.entities.User;

public interface UserDao extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
	Optional<User> findByEmailAndPasswordAndRole(String email, String password, Role role);
	
	boolean existsByRole(Role role);
}
