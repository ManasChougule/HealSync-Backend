package com.java.loginReg.api.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.java.loginReg.business.abstracts.AppointmentService;
import com.java.loginReg.business.abstracts.UserService;
import com.java.loginReg.entities.Appointment;
import com.java.loginReg.entities.Doctor;
import com.java.loginReg.entities.Role;
import com.java.loginReg.entities.User;
import com.java.loginReg.entities.UserDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AppointmentService appointmentService;

	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "register";
	}

	@PostMapping("/registration")
	public User saveUser(@RequestBody UserDto userDto) {
		User user = userService.save(userDto);
		return user;
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
		boolean isAuthenticated = userService.authenticate(userDto.getEmail(), userDto.getPassword(), userDto.getRole());

		if (isAuthenticated) {
			switch (userDto.getRole()) {
				case ADMIN:
					return ResponseEntity.ok("Admin Home Page");
				case DOCTOR:
					return ResponseEntity.ok("Doctor Home Page");
				case PATIENT:
					return ResponseEntity.ok("Patient Home Page");
				default:
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or role");
		}
	}

	// Endpoint to retrieve all users
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers(@RequestParam Role role) {
		if (role != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	// Endpoint to delete a user
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		try {
			userService.deleteUser(id);  // Perform user deletion
			return ResponseEntity.ok("User deleted successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(400).body("User not found!");
		}
	}

	// Endpoint to update a user
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
		boolean isUpdated = userService.updateUser(id, user);
		if(isUpdated) {
			return ResponseEntity.ok("User updated successfully!");
		} else {
			return ResponseEntity.status(400).body("User not found!");
		}
	}

	// Endpoint to retrieve all appointments
	@GetMapping("/appointments")
	public ResponseEntity<List<Appointment>> getAllAppointments(@RequestParam Role role) {
		if (role != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		List<Appointment> appointments = appointmentService.getAllAppointments();
		return ResponseEntity.ok(appointments);
	}

	// Endpoint to delete an appointment
	@DeleteMapping("/appointments/delete/{id}")
	public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
		boolean isDeleted = appointmentService.deleteAppointment(id);
		if(isDeleted) {
			return ResponseEntity.ok("Appointment deleted successfully!");
		} else {
			return ResponseEntity.status(400).body("Appointment not found!");
		}
	}

	// When a user logs in, returns ID and full name based on role
	@GetMapping("/getUserId")
	public ResponseEntity<Map<String, Object>> getUserId(
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam Role role) {

		Map<String, Object> response = userService.getUserIdByCredentials(email, password, role);

		if (response != null) {
			return ResponseEntity.ok(response); // Successful response
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "User not found"));
		}
	}
}