package com.java.healSync.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entity.Doctor;
import com.java.healSync.entity.Specialization;
import com.java.healSync.entity.User;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	Doctor findByUserEmail(String email);
	
	List<Doctor> findBySpecialization(Specialization specialization);
	
	Optional<Doctor> findByUser(User user);
	
	void deleteByUser(User user);

	Optional<Doctor> findByUserId(Long userId);

	boolean existsByUserId(Long userId);

}
