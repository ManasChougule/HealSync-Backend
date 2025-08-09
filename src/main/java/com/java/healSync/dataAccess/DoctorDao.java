package com.java.healSync.dataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entities.Doctor;
import com.java.healSync.entities.Specialization;
import com.java.healSync.entities.User;

public interface DoctorDao extends JpaRepository<Doctor, Long> {

	Doctor findByUserEmail(String email);
	
	List<Doctor> findBySpecialization(Specialization specialization);
	
	Optional<Doctor> findByUser(User user);
	
	void deleteByUser(User user);

	Optional<Object> findByUserId(Long userId);

	boolean existsByUserId(Long userId);

}
