package com.java.loginReg.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.java.loginReg.business.abstracts.HospitalService;
import com.java.loginReg.dataAccess.UserDao;
import com.java.loginReg.entities.AmbulanceBookingDao;
import com.java.loginReg.entities.Hospital;
import com.java.loginReg.entities.HospitalRequestDto;
import com.java.loginReg.entities.User;

@RestController
@RequestMapping("/hospitals")
@CrossOrigin(origins = "http://localhost:3000")
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private UserDao userDao;

	// Endpoint to add a hospital
	@PostMapping("/add")
	public ResponseEntity<Hospital> addHospital(@RequestBody Hospital hospital) {
		Hospital savedHospital = hospitalService.addHospital(hospital);
		return ResponseEntity.ok(savedHospital);
	}

	// Endpoint to retrieve all hospitals
	@GetMapping("/all")
	public ResponseEntity<List<Hospital>> getAllHospitals() {
		List<Hospital> hospitals = hospitalService.getAllHospitals();
		return ResponseEntity.ok(hospitals);
	}

	// Endpoint to delete a hospital
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteHospital(@PathVariable Long id) {
		boolean isDeleted = hospitalService.deleteHospital(id);
		if (isDeleted) {
			return ResponseEntity.ok("Hospital deleted successfully!");
		} else {
			return ResponseEntity.status(400).body("Hospital not found!");
		}
	}

	// Endpoint to update a hospital
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateHospital(@PathVariable Long id,
												 @RequestBody HospitalRequestDto hospitalRequestDto) {
		boolean isUpdated = hospitalService.updateHospital(id, hospitalRequestDto);
		if (isUpdated) {
			return ResponseEntity.ok("Hospital updated successfully!");
		} else {
			return ResponseEntity.status(400).body("Hospital not found!");
		}
	}

	// ✅ Ambulance Booking Endpoint
	@PostMapping("/book-ambulance")
	public ResponseEntity<String> bookAmbulance(@RequestBody AmbulanceBookingDao request) {
		Optional<User> userOpt = userDao.findById(request.getPatientId());

		if (userOpt.isPresent()) {
			User patient = userOpt.get();
			String destination = request.getDestination() != null && !request.getDestination().isEmpty()
					? request.getDestination()
					: "Hospital";

			String message = "Ambulance booked successfully for patient " + patient.getFirstName() +
					". Pickup location: " + request.getPickupLocation() +
					". Destination: " + destination + ".";

			return ResponseEntity.ok(message);
		} else {
			return ResponseEntity.status(404).body("Patient not found with ID: " + request.getPatientId());
		}
	}
}
