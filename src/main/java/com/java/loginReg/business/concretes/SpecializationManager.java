package com.java.loginReg.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.loginReg.business.abstracts.SpecializationService;
import com.java.loginReg.dataAccess.SpecializationDao;
import com.java.loginReg.entities.Specialization;
import com.java.loginReg.entities.SpecializationRequestDto;

@Service
public class SpecializationManager implements SpecializationService{

	@Autowired
	private SpecializationDao specializationDao;

	// Method for registering a specialization
	@Override
	public Specialization addSpecialization(Specialization specialization) {
		return specializationDao.save(specialization);
	}

	// Method returning the list of all specializations
	@Override
	public List<Specialization> getAllSpecializations() {
		return specializationDao.findAll();
	}

	// Method to retrieve a doctor by specialization
	public Specialization findByName(String name) {
		return specializationDao.findByName(name)
				.orElseThrow(() -> new IllegalArgumentException("Specialization not found with name: " + name));
	}

	// Method for deleting a specialization
	@Override
	public boolean deleteSpecialization(Long id) {
		if (specializationDao.existsById(id)) { // If specialization exists in the database
			specializationDao.deleteById(id); // Delete the specialization
			return true;
		}
		return false;
	}

	// Method for updating a specialization
	@Override
	public boolean updateSpecialization(Long id, SpecializationRequestDto specializationRequestDto) {
		Specialization existingSpecialization = specializationDao.findById(id).orElseThrow();
		if (existingSpecialization != null) {
			existingSpecialization.setName(specializationRequestDto.getName());
			specializationDao.save(existingSpecialization);
			return true;
		}
		return false;
	}
}