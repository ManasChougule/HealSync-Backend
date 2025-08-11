package com.java.healSync.service.services;

import java.util.List;
import java.util.Map;

import com.java.healSync.dto.AuthRequestDto;
import com.java.healSync.dto.AuthResponseDto;
import org.springframework.stereotype.Service;

import com.java.healSync.enums.Role;
import com.java.healSync.entity.User;
import com.java.healSync.dto.UserDto;

public interface UserService {
	
	List<User> getAllUsers();
	
	User save(UserDto userDto);

	AuthResponseDto authenticateAndGetToken(AuthRequestDto authRequestDto);

	void deleteUser(Long userId);
	
	boolean updateUser(Long id, User user);
	
	User getUserByEmail(String email);

	Map<String, Object> getUserIdByCredentials(String email, String password, Role role);
}
