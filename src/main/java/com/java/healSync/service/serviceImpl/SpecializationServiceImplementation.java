package com.java.healSync.service.serviceImpl;

import java.util.List;

import com.java.healSync.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.healSync.service.services.SpecializationService;
import com.java.healSync.entity.Specialization;
import com.java.healSync.dto.SpecializationRequestDto;

@Service
public class SpecializationServiceImplementation implements SpecializationService{

	@Autowired
	private SpecializationRepository specializationRepository;

	// Method for registering a specialization
	@Override
	public Specialization addSpecialization(Specialization specialization) {
		return specializationRepository.save(specialization);
	}

	// Method returning the list of all specializations
	@Override
	public List<Specialization> getAllSpecializations() {
		return specializationRepository.findAll();
	}

	// Method to retrieve a doctor by specialization
	public Specialization findByName(String name) {
		return specializationRepository.findByName(name)
				.orElseThrow(() -> new IllegalArgumentException("Specialization not found with name: " + name));
	}

	// Method for deleting a specialization
	@Override
	public boolean deleteSpecialization(Long id) {
		if (specializationRepository.existsById(id)) { // If specialization exists in the database
			specializationRepository.deleteById(id); // Delete the specialization
			return true;
		}
		return false;
	}

	// Method for updating a specialization
	@Override
	public boolean updateSpecialization(Long id, SpecializationRequestDto specializationRequestDto) {
		Specialization existingSpecialization = specializationRepository.findById(id).orElseThrow();
		if (existingSpecialization != null) {
			existingSpecialization.setName(specializationRequestDto.getName());
			specializationRepository.save(existingSpecialization);
			return true;
		}
		return false;
	}
}