package com.java.healSync.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.java.healSync.business.abstracts.HospitalService;
import com.java.healSync.dataAccess.UserDao;
import com.java.healSync.entities.Hospital;
import com.java.healSync.entities.HospitalRequestDto;

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


	}

