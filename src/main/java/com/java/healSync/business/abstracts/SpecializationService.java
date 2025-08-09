package com.java.healSync.business.abstracts;

import java.util.List;

import com.java.healSync.entities.Specialization;
import com.java.healSync.entities.SpecializationRequestDto;

public interface SpecializationService {

	Specialization addSpecialization(Specialization specialization);
	
	List<Specialization> getAllSpecializations();
	
	Specialization findByName(String name);
	
	boolean deleteSpecialization(Long id);
	
	boolean updateSpecialization(Long id, SpecializationRequestDto specializationRequestDto);
}
