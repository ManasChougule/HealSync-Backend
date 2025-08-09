package com.java.healSync.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.healSync.service.services.HospitalService;
import com.java.healSync.repository.HospitalRepository;
import com.java.healSync.entity.Hospital;
import com.java.healSync.dto.HospitalRequestDto;

@Service
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	private HospitalRepository hospitalDao;

	// Method for hospital registration
	@Override
	public Hospital addHospital(Hospital hospital) {
		return hospitalDao.save(hospital);
	}

	// Method returning the list of all hospitals
	@Override
	public List<Hospital> getAllHospitals() {
		return hospitalDao.findAll();
	}

	// Method for deleting a hospital
	@Override
	public boolean deleteHospital(Long id) {
		if (hospitalDao.existsById(id)) { // If hospital exists in the database
			hospitalDao.deleteById(id); // Delete the hospital
			return true;
		}
		return false;
	}

	// Method for updating a hospital
	@Override
	public boolean updateHospital(Long id, HospitalRequestDto hospitalRequestDto) {
		Hospital existingHospital = hospitalDao.findById(id).orElseThrow(); // Retrieve existing hospital details from the database
		if (existingHospital != null) { // If hospital exists in the database
			existingHospital.setName(hospitalRequestDto.getName()); // Update with the new name
			existingHospital.setCity(hospitalRequestDto.getCity()); // Update with the new city
			hospitalDao.save(existingHospital); // Save the updated hospital record
			return true;
		}
		return false;
	}
}