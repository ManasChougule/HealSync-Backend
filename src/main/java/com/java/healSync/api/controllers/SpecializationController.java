package com.java.healSync.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.healSync.business.abstracts.SpecializationService;
import com.java.healSync.entities.Specialization;
import com.java.healSync.entities.SpecializationRequestDto;

@RestController
@RequestMapping("/specializations")
@CrossOrigin(origins = "http://localhost:3000")
public class SpecializationController {

	@Autowired
	private SpecializationService specializationService;

	// Endpoint to add a specialization
	@PostMapping("/add")
	public ResponseEntity<Specialization> addHospital(@RequestBody SpecializationRequestDto specializationDto) {
		Specialization specialization = new Specialization();
		specialization.setName(specializationDto.getName());
		specialization.setId(specializationDto.getId()); // optional

		Specialization savedSpecialization = specializationService.addSpecialization(specialization);
		return ResponseEntity.ok(savedSpecialization);
	}

	// Endpoint to retrieve all specializations
	@GetMapping("/all")
	public ResponseEntity<List<Specialization>> getAllSpecializations() {
		List<Specialization> specializations = specializationService.getAllSpecializations();
		return ResponseEntity.ok(specializations);
	}

	// Endpoint to delete a specialization
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteSpecialization(@PathVariable Long id) {
		boolean isDeleted = specializationService.deleteSpecialization(id);
		if(isDeleted) {
			return ResponseEntity.ok("Specialization deleted successfully!");
		} else {
			return ResponseEntity.status(400).body("Specialization not found!");
		}
	}

	// Endpoint to update a specialization
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateSpecialization(@PathVariable Long id, @RequestBody SpecializationRequestDto specializationRequestDto) {
		boolean isUpdated = specializationService.updateSpecialization(id, specializationRequestDto);
		if(isUpdated) {
			return ResponseEntity.ok("Specialization updated successfully!");
		} else {
			return ResponseEntity.status(400).body("Specialization not found!");
		}
	}
}