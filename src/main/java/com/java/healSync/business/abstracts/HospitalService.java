package com.java.healSync.business.abstracts;

import java.util.List;

import com.java.healSync.entities.Hospital;
import com.java.healSync.entities.HospitalRequestDto;

public interface HospitalService {

	Hospital addHospital(Hospital hospital);
	
	List<Hospital> getAllHospitals();
	
	boolean deleteHospital(Long id);
	
	boolean updateHospital(Long id, HospitalRequestDto hospitalRequestDto);
}
