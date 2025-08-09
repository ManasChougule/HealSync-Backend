package com.java.healSync.service.services;

import java.util.List;

import com.java.healSync.entity.Doctor;
import com.java.healSync.dto.DoctorDto;
import com.java.healSync.entity.Specialization;

public interface DoctorService {

	List<Doctor> getAllDoctors();
	
	Doctor save(Doctor doctor);
	
	Doctor getDoctorById(Long id);
	
	List<Doctor> findBySpecialization(Specialization specialization);
	
	boolean updateDoctor(Long id, DoctorDto doctorDto);
	

}
