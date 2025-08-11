package com.java.healSync.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.java.healSync.dto.AuthRequestDto;
import com.java.healSync.dto.AuthResponseDto;
import com.java.healSync.repository.DoctorRepository;
import com.java.healSync.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.java.healSync.service.services.AppointmentService;
import com.java.healSync.service.services.UserService;
import com.java.healSync.entity.Appointment;
import com.java.healSync.enums.Role;
import com.java.healSync.entity.User;
import com.java.healSync.dto.UserDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DoctorRepository doctorRepository;

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
	public ResponseEntity<AuthResponseDto> loginUser(@RequestBody AuthRequestDto authRequestDto) {
		AuthResponseDto response = userService.authenticateAndGetToken(authRequestDto);
		return ResponseEntity.ok(response);
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

	@GetMapping("/patientId-by-user/{userId}")
	public ResponseEntity<Long> getPatientIdByUserId(@PathVariable Long userId) {
		return patientRepository.findByUserId(userId)
				.map(patient -> ResponseEntity.ok(patient.getId()))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/doctorId-by-user/{userId}")
	public ResponseEntity<Long> getDoctorIdByUserId(@PathVariable Long userId) {
		return doctorRepository.findByUserId(userId)
				.map(doctor -> ResponseEntity.ok(doctor.getId()))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

}