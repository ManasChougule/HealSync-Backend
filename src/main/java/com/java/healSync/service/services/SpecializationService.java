package com.java.healSync.service.services;

import java.util.List;

import com.java.healSync.entity.Specialization;
import com.java.healSync.dto.SpecializationRequestDto;

public interface SpecializationService {

	Specialization addSpecialization(Specialization specialization);
	
	List<Specialization> getAllSpecializations();
	
	Specialization findByName(String name);
	
	boolean deleteSpecialization(Long id);
	
	boolean updateSpecialization(Long id, SpecializationRequestDto specializationRequestDto);
}
