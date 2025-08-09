package com.java.healSync.service.services;

import java.util.List;

import com.java.healSync.entity.Hospital;
import com.java.healSync.dto.HospitalRequestDto;

public interface HospitalService {

	Hospital addHospital(Hospital hospital);
	
	List<Hospital> getAllHospitals();
	
	boolean deleteHospital(Long id);
	
	boolean updateHospital(Long id, HospitalRequestDto hospitalRequestDto);
}
