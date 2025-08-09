package com.java.healSync.business.abstracts;

import java.util.List;

import com.java.healSync.entities.Doctor;
import com.java.healSync.entities.DoctorDto;
import com.java.healSync.entities.Specialization;

public interface DoctorService {

	List<Doctor> getAllDoctors();
	
	Doctor save(Doctor doctor);
	
	Doctor getDoctorById(Long id);
	
	List<Doctor> findBySpecialization(Specialization specialization);
	
	boolean updateDoctor(Long id, DoctorDto doctorDto);
	

}
